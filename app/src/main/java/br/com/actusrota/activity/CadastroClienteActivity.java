package br.com.actusrota.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.HashSet;

import br.com.actusrota.R;
import br.com.actusrota.dao.ClienteDAO;
import br.com.actusrota.entidade.Cidade;
import br.com.actusrota.entidade.Cliente;
import br.com.actusrota.entidade.Endereco;
import br.com.actusrota.entidade.Telefone;
import br.com.actusrota.enumerador.EnumTipoTelefone;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilMensagem;
import br.com.actusrota.util.UtilString;

public class CadastroClienteActivity extends SuperActivity<Cliente> {

	private ObterValor obterValor;
	private Cliente cliente;
	private Cidade cidade;
	private ClienteDAO clienteDAO;
	private boolean irParaVenda;

	public CadastroClienteActivity() {
		obterValor = new ObterValor(this);
		clienteDAO = new ClienteDAO(this);
		cliente = new Cliente();
		cliente.setEndereco(new Endereco());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cadcliente);
		try {
			obterParametros();
			criarComponentes();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarComponentes() {
		try {
//			final EditText edtCpf = (EditText) findViewById(R.id.cadcli_edtCPF);
//			edtCpf.setInputType(InputType.TYPE_NULL);
//			obterValor.adicionarMascara(edtCpf, "###.###.###-##");

//			final EditText edtNumero = (EditText) findViewById(R.id.cadcli_edtNumeroRes);
//			// edtCpf.setInputType(InputType.TYPE_NULL);
//			obterValor.adicionarMascara(edtNumero, "####-####");

			if (cidade != null)
				obterValor.getTextView(R.id.cadcli_txvCidade).setText(
						cidade.toString());
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void obterParametros() {
		Object obj = obeterParametrosActivity();
		if (obj != null && obj.getClass().isAssignableFrom(Cidade.class)) {
			cidade = (Cidade) obj;
			return;
		}
		if (obj != null && obj.getClass().isAssignableFrom(Cidade.class)) {
			cliente = (Cliente) obj;
			setarDadosTela();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_cliente, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mc_salvar:
			irParaVenda = false;
			salvarCliente();
			return true;
		case R.id.mc_salvarAbrirVenda:
			irParaVenda = true;
			salvarCliente();
			return true;
		case R.id.mcid_voltar:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void salvarCliente() {
		if (cidade == null) {
			UtilMensagem.mostarMensagemLonga(this, "Cidade n�o carregada.");
			return;
		}
		if (obterValor.isInValidoValorEdit(R.id.cadcli_edtCPF)) {
			UtilMensagem.mostarMensagemLonga(this, "Por favor, informe o CPF.");
			return;
		}

		if (!UtilString.validarCPF(UtilString.removerMaskCPF(obterValor
				.getValorEdit(R.id.cadcli_edtCPF)))) {
			UtilMensagem.mostarMensagemLonga(this, "CPF inválido.");
			return;
		}

		if (obterValor.isInValidoValorEdit(R.id.cadcli_edtNome)) {
			UtilMensagem
					.mostarMensagemLonga(this, "Por favor, informe o Nome.");
			return;
		}
		if (obterValor.isInValidoValorEdit(R.id.cadcli_edtLogradouro)) {
			UtilMensagem.mostarMensagemLonga(this,
					"Por favor, informe o Logradouro.");
			return;
		}
		if (obterValor.isInValidoValorEdit(R.id.cadcli_edtBairro)) {
			UtilMensagem.mostarMensagemLonga(this,
					"Por favor, informe o Bairro.");
			return;
		}
		criarDialogSalvar();
	}

	protected void criarDialogSalvar() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Confirme")
				.setMessage("Deseja realmente salvar?")
				.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								salvar();
							}

						}).setNegativeButton("N�o", null).show();
	}

	@Override
	protected void salvar() {
		try {
			montarCliente();
			clienteDAO.adicionarNovo(cliente);
			UtilMensagem
					.mostarMensagemLonga(this, "Cliente salvo com sucesso!");
			clienteDAO.abrirBanco();
			Long maxIdCliente = clienteDAO.buscarMaxID();
			if (irParaVenda) {
				criarTelaVenda(maxIdCliente);
			} else {
				Thread.sleep(1000);
				finish();
			}
		} catch (Exception e) {
			tratarExibirErro(e);
		} finally {
			clienteDAO.fecharBanco();
		}
	}

	private void criarTelaVenda(Long maxIdCliente) {
		try {
			cliente = clienteDAO.consultarPorId(maxIdCliente);
			criarActivityComParametro(VendaActivity.class, cliente);
			System.out.println("teste:" + cliente);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void setarDadosTela() {
		try {
			// cliente.setCidade(cidade);
			obterValor.setTextoEditText(cliente.getNome(), R.id.cadcli_edtNome);

			obterValor.setTextoEditText(cliente.getCpf(), R.id.cadcli_edtCPF);

			obterValor.setTextoEditText(cliente.getRg(), R.id.cadcli_edtRG);

			obterValor.setTextoEditText(cliente.getEndereco().getLogradouro(),
					R.id.cadcli_edtLogradouro);

			obterValor.setTextoEditText(cliente.getEndereco().getBairro(),
					R.id.cadcli_edtBairro);

			obterValor.setTextoEditText(cliente.getEndereco().getCep(),
					R.id.cadcli_edtCep);

			obterValor.setTextoEditText(cliente.getEndereco()
					.getPontoReferencia(), R.id.cadcli_edtPontoReferencia);

			// cliente.getEndereco().setCidade(cidade);

			for (Telefone telefone : cliente.getTelefones()) {

				switch (telefone.getTipoTelefone()) {
				case CELULAR:
					obterValor.setTextoEditText(telefone.getDdd(),
							R.id.cadcli_edtDDDCel);
					obterValor.setTextoEditText(telefone.getNumero(),
							R.id.cadcli_edtNumeroCel);
					break;
				case COMERCIAL:
					obterValor.setTextoEditText(telefone.getDdd(),
							R.id.cadcli_edtDDDCom);
					obterValor.setTextoEditText(telefone.getNumero(),
							R.id.cadcli_edtNumeroCom);
					break;
				case RESIDENCIAL:
					obterValor.setTextoEditText(telefone.getDdd(),
							R.id.cadcli_edtDDDRes);
					obterValor.setTextoEditText(telefone.getNumero(),
							R.id.cadcli_edtNumeroRes);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void montarCliente() {
		try {
			cliente.setCidade(cidade);
			cliente.setNome(obterValor.getValorEdit(R.id.cadcli_edtNome));
			cliente.setCpf(UtilString.removerMaskCPF(obterValor
					.getValorEdit(R.id.cadcli_edtCPF)));
			cliente.setRg(obterValor.getValorEdit(R.id.cadcli_edtRG));

			cliente.getEndereco().setCidade(cidade);
			cliente.getEndereco().setLogradouro(
					obterValor.getValorEdit(R.id.cadcli_edtLogradouro));
			cliente.getEndereco().setBairro(
					obterValor.getValorEdit(R.id.cadcli_edtBairro));
			cliente.getEndereco().setCep(
					obterValor.getValorEdit(R.id.cadcli_edtCep));
			cliente.getEndereco().setPontoReferencia(
					obterValor.getValorEdit(R.id.cadcli_edtPontoReferencia));

			cliente.setTelefones(new HashSet<Telefone>(0));
			if (!obterValor.isInValidoValorEdit(R.id.cadcli_edtDDDRes)) {
				if (obterValor.isInValidoValorEdit(R.id.cadcli_edtNumeroRes)) {
					UtilMensagem.mostarMensagemLonga(this,
							"O N� de Telefone Residencial deve ser informado.");
					return;
				}
				Telefone telefone = new Telefone();
				telefone.setTipoTelefone(EnumTipoTelefone.RESIDENCIAL);
				telefone.setDdd(obterValor.getValorEdit(R.id.cadcli_edtDDDRes));
				telefone.setNumero(obterValor
						.getValorEdit(R.id.cadcli_edtNumeroRes));
				telefone.setCliente(cliente);

				cliente.getTelefones().add(telefone);
			}

			if (!obterValor.isInValidoValorEdit(R.id.cadcli_edtDDDCel)) {
				if (obterValor.isInValidoValorEdit(R.id.cadcli_edtNumeroCel)) {
					UtilMensagem.mostarMensagemLonga(this,
							"O N� de Telefone Celular deve ser informado.");
					return;
				}
				Telefone telefone = new Telefone();
				telefone.setTipoTelefone(EnumTipoTelefone.CELULAR);
				telefone.setDdd(obterValor.getValorEdit(R.id.cadcli_edtDDDCel));
				telefone.setNumero(obterValor
						.getValorEdit(R.id.cadcli_edtNumeroCel));
				telefone.setCliente(cliente);

				cliente.getTelefones().add(telefone);
			}

			if (!obterValor.isInValidoValorEdit(R.id.cadcli_edtDDDCom)) {
				if (obterValor.isInValidoValorEdit(R.id.cadcli_edtNumeroCom)) {
					UtilMensagem.mostarMensagemLonga(this,
							"O N� de Telefone Comercial deve ser informado.");
					return;
				}
				Telefone telefone = new Telefone();
				telefone.setTipoTelefone(EnumTipoTelefone.COMERCIAL);
				telefone.setDdd(obterValor.getValorEdit(R.id.cadcli_edtDDDCom));
				telefone.setNumero(obterValor
						.getValorEdit(R.id.cadcli_edtNumeroCom));
				telefone.setCliente(cliente);

				cliente.getTelefones().add(telefone);
			}
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	// public void eventoCliqueRadioButton(View view) {
	// boolean selecionadio = ((RadioButton) view).isChecked();
	// RadioButton rdbResidencial = (RadioButton)
	// findViewById(R.id.cadcli_rdbResidencial);
	// RadioButton rdbCelular = (RadioButton)
	// findViewById(R.id.cadcli_rdbCelular);
	// RadioButton rdbComercial = (RadioButton)
	// findViewById(R.id.cadcli_rdbComercial);
	//
	// switch (view.getId()) {
	// case R.id.cadcli_rdbResidencial:
	// if (selecionadio) {
	// tipoTelefone = EnumTipoTelefone.RESIDENCIAL;
	// rdbResidencial.setChecked(true);
	// rdbCelular.setChecked(false);
	// rdbComercial.setChecked(false);
	// }
	// break;
	// case R.id.cadcli_rdbCelular:
	// if (selecionadio) {
	// tipoTelefone = EnumTipoTelefone.CELULAR;
	// rdbCelular.setChecked(true);
	// rdbComercial.setChecked(false);
	// rdbResidencial.setChecked(false);
	// }
	// break;
	// case R.id.cadcli_rdbComercial:
	// if (selecionadio) {
	// tipoTelefone = EnumTipoTelefone.COMERCIAL;
	// rdbComercial.setChecked(true);
	// rdbResidencial.setChecked(false);
	// rdbCelular.setChecked(false);
	// }
	// break;
	//
	// }
	// }

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
}
