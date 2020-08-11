package br.com.actusrota.activity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import br.com.actusrota.R;
import br.com.actusrota.dao.PagmentoDinherioDAO;
import br.com.actusrota.dao.VendaDAO;
import br.com.actusrota.entidade.PagamentoEspecie;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.enumerador.EnumStatusVenda;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilDates;
import br.com.actusrota.util.UtilMensagem;

public class PagDinherioActivity extends SuperActivity<PagamentoEspecie> {

	private VendaDAO vendaDAO;
	private Venda venda;
	private ObterValor obterValor;
	private PagmentoDinherioDAO pagDinherioDAO;
	private PagamentoEspecie pagamento;

	public PagDinherioActivity() {
		this.vendaDAO = new VendaDAO(this);
		pagDinherioDAO = new PagmentoDinherioDAO(this);
		obterValor = new ObterValor(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recebimento_dinheiro);
		try {
			obterValoresParametros();
			criarComponentes();
			setarDadosPagamento();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void obterValoresParametros() {
		try {
			Bundle b = getIntent().getExtras();

			Object object = b.get("parametro");
			if (object != null) {
				venda = (Venda) object;
			}
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void carregarVenda() {
		if (obterValor.isInValidoValorEdit(R.id.din_edtNumVenda)) {
			mostarMensagem("O código da venda deve ser informado.");
			return;
		}
		try {
			venda = vendaDAO.carregarVendaReceber(Long.valueOf(obterValor
					.getValorEdit(R.id.din_edtNumVenda)));

			setarDadosPagamento();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void setarDadosPagamento() {
		if (venda != null && !venda.isNovo()) {
			// venda.somarTodosItens();1
			setarDados();
		} else {
			mostarMensagem("Venda não encontrada.");
		}
	}

	private void setarDados() {
		try {
			obterValor.setTextoTextView(venda.getCliente().getNome(),
					R.id.din_txvCliente);
			obterValor.setTextoTextView(venda.getValorTotal()
					.getValorFormatado(), R.id.din_txvTotalVenda);

			String valorFormatado = venda.getValorReceber().getValorFormatado();
			obterValor.setTextoTextView(valorFormatado, R.id.din_txvReceber);

			// sugestão de pagamento(pode confundir o vendedor quando ele
			// visualizar o detalhamento
			// obterValor.setTextoTextView(valorFormatado, R.id.din_edtValor);

			obterValor.setTextoTextView(venda.getValorTotalEspecie()
					.getValorFormatado(), R.id.din_txvJaRecebido);
			
			obterValor.setTextoTextView(venda.getValorTotalDevolucao()
					.getValorFormatado(), R.id.din_txvDevolucao);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarComponentes() {
		try {
			EditText edtData = obterValor.getEditText(R.id.din_edtData);
			edtData.setText(UtilDates.formatarData(new Date()));

			obterValor.adicionarMascara(edtData, "##/##/####");

			// obterValor.desabilitarTecladoVirtual(R.id.din_edtValor);
			// obterValor.desabilitarTecladoVirtual(R.id.din_edtData);
			// obterValor.desabilitarTecladoVirtual(R.id.din_edtNumVenda);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	public void botaoClicado(View v) {
		switch (v.getId()) {
		case R.id.din_btnPequisarVenda:
			carregarVenda();
			break;
		case R.id.din_btnSalvar:
			salvarPagamentoDinheiro();
			break;
		}
	}

	private void salvarPagamentoDinheiro() {
		if (venda == null) {
			UtilMensagem.mostarMensagemLonga(this,
					"O Nº da venda deve ser informado.");
			return;
		}

		if (obterValor.isInValidoValorEdit(R.id.din_edtValor)) {
			UtilMensagem.mostarMensagemLonga(this,
					"O valor recebido deve ser informado.");
			return;
		}
		pagamento = definirDadosPagamento();
		if (pagamento == null || pagamento.getValor().isNovo()) {
			UtilMensagem.mostarMensagemLonga(this,
					"Pagamento não pode ser gerado.");
			return;
		}
		criarDialogSalvar();
	}

	@Override
	protected void salvar() {
		try {
			
			EnumStatusVenda statusVenda = venda.getStatusVenda();
			pagamento.setStatusVenda(statusVenda);
			
			pagDinherioDAO.adicionar(pagamento);
			UtilMensagem.mostarMensagemLonga(this,
					"Pagamento salvo com sucesso!");

			Set<PagamentoEspecie> pagamentos = new HashSet<PagamentoEspecie>(1);
			pagamentos.add(pagamento);
			venda.setPagamentosEspecie(pagamentos);

			// FECHAR A VENDA
			venda.fecharVenda();
			
			vendaDAO.atualizar(venda.getId(), venda);
			Thread.sleep(1000);
			finish();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private PagamentoEspecie definirDadosPagamento() {
		try {
			PagamentoEspecie pagamentoEspecie = new PagamentoEspecie();
			pagamentoEspecie.setIdVenda(venda.getId());
			pagamentoEspecie.setIdViagem(venda.getViagem().getId());
			pagamentoEspecie.setComissaoPaga(false);

			String valorEdtData = obterValor.getValorEdit(R.id.din_edtData);
			pagamentoEspecie.setDataPagamento(UtilDates
					.formatarData(valorEdtData));

			pagamentoEspecie.setValor(obterValor
					.getValorMonetarioEdit(R.id.din_edtValor));
			return pagamentoEspecie;
		} catch (Exception e) {
			tratarExibirErro(e);
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_receb_dinheiro, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnp_voltar:
			finish();
			return true;
		case R.id.mnpgd_detalhamento_venda:
			criarDetalhamentoDeVenda();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void criarDetalhamentoDeVenda() {
		try {
			venda = vendaDAO.carregarVendaCompleta(venda.getId());
			venda.setValorTotalEspecie(obterValor
					.getValorMonetarioEdit(R.id.din_edtValor));
			venda.somarTodosItens();
			criarActivityComParametro(DetalhamentoVenda.class, venda);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Pagamento em dinheiro")
				.setMessage("Deseja realmente sair?")
				.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}

						}).setNegativeButton("Não", null).show();
	}

}
