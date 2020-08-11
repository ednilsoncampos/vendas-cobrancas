package br.com.actusrota.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import br.com.actusrota.R;
import br.com.actusrota.adapter.PagamentoAdapter;
import br.com.actusrota.dao.PagmentoChequeDAO;
import br.com.actusrota.dao.VendaDAO;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.PagamentoCheque;
import br.com.actusrota.entidade.Venda;
import br.com.actusrota.enumerador.EnumFormaPagamento;
import br.com.actusrota.util.Mascara;
import br.com.actusrota.util.ObterValor;
import br.com.actusrota.util.UtilDates;
import br.com.actusrota.util.UtilMensagem;

public class PagChequeActivity extends SuperActivity<PagamentoCheque> {

	private VendaDAO vendaDAO;
	private Venda venda;
	private ObterValor obterValor;
	private PagmentoChequeDAO pagmentoChequeDAO;
	private PagamentoAdapter adapter;
	private EnumFormaPagamento formaPagamento;
	private List<PagamentoCheque> pagamentos;

	public PagChequeActivity() {
		this.vendaDAO = new VendaDAO(this);
		pagmentoChequeDAO = new PagmentoChequeDAO(this);
		obterValor = new ObterValor(this);
		formaPagamento = EnumFormaPagamento.A_PRAZO;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recebimento_cheque);
		try {
			pagamentos = new ArrayList<PagamentoCheque>();
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

//	private void carregarVenda() {
//		if (obterValor.isInValidoValorEdit(R.id.chq_edtNumVenda)) {
//			mostarMensagem("O código da venda deve ser informado.");
//			return;
//		}
//		try {
//			venda = vendaDAO.carregarVendaReceber(Long.valueOf(obterValor
//					.getValorEdit(R.id.chq_edtNumVenda)));
//			setarDadosPagamento();
//		} catch (Exception e) {
//			tratarExibirErro("Venda não encontrada.", e);
//		}
//	}

	private void setarDadosPagamento() {
		if (venda != null && !venda.isNovo()) {
			// venda.somarTodosItens();
			if (!venda.isSemPagamentoCheque()) {
				pagamentos = new ArrayList<PagamentoCheque>(
						venda.getPagamentosCheque());
				criarAdapter();
			}
			setarDados();
		} else {
			mostarMensagem("Venda não encontrada.");
		}
	}

	private void setarDados() {
		try {
			obterValor.setTextoTextView(venda.getCliente().getNome(),
					R.id.chq_txvCliente);

			obterValor.setTextoTextView(venda.getValorTotal()
					.getValorFormatado(), R.id.chq_txvTotalVenda);

			obterValor.setTextoTextView(venda.getValorReceber()
					.getValorFormatado(), R.id.chq_txvReceber);

			obterValor.setTextoEditText(String.valueOf(pagamentos.size()),
					R.id.chq_edtQtdParcela);

			obterValor.getEditText(R.id.chq_edtNumCheque).requestFocus();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarComponentes() {
		try {
			EditText edtData = obterValor.getEditText(R.id.chq_edtData);
			edtData.setText(UtilDates.formatarData(new Date()));
			edtData.addTextChangedListener(Mascara
					.insert("##/##/####", edtData));

			// obterValor.desabilitarTecladoVirtual(R.id.chq_edtData);
			// obterValor.desabilitarTecladoVirtual(R.id.chq_edtNumVenda);
			// obterValor.desabilitarTecladoVirtual(R.id.chq_edtNumCheque);
			// obterValor.desabilitarTecladoVirtual(R.id.chq_edtQtdParcela);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	public void botaoClicado(View v) {
		switch (v.getId()) {
//		case R.id.chq_btnPequisarVenda:
//			carregarVenda();
//			break;
		case R.id.chq_btnGerarParcela:
			gerarParcela();
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void gerarParcela() {
		if (venda == null) {
			UtilMensagem.mostarMensagemLonga(this,
					"O Nº da venda deve ser informado.");
			return;
		}

		if (obterValor.isInValidoValorEdit(R.id.chq_edtNumCheque)) {
			UtilMensagem.mostarMensagemLonga(this,
					"O número do cheque deve ser informada.");
			return;
		}

		if (obterValor.isInValidoValorEdit(R.id.chq_edtQtdParcela)) {
			UtilMensagem.mostarMensagemLonga(this,
					"A quantidade de parcelas deve ser informada.");
			return;
		}

		try {
			String qtdParcela = obterValor.getValorEdit(R.id.chq_edtQtdParcela);
			String dataPagamento = obterValor.getValorEdit(R.id.chq_edtData);
			String numCheque = obterValor.getValorEdit(R.id.chq_edtNumCheque);
			Dinheiro aReceber = obterValor
					.getValorMonetarioTextView(R.id.chq_txvReceber);

			pagamentos = (List<PagamentoCheque>) formaPagamento.gerarParcelas(
					aReceber, Integer.parseInt(qtdParcela),
					UtilDates.formatarData(dataPagamento), numCheque,
					venda.getId(), venda.getViagem().getId());

			criarAdapter();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	private void criarAdapter() {
		if (pagamentos == null)
			pagamentos = new ArrayList<PagamentoCheque>();
		try {
			adapter = new PagamentoAdapter(this, pagamentos);

			final ListView listProdutos = (ListView) findViewById(R.id.chq_ltv_pagametos);

			listProdutos.setAdapter(adapter);
		} catch (Exception e) {
			tratarExibirErro(e);
		}

	}

	private void salvarPagamentoCheque() {
		if (venda == null) {
			UtilMensagem.mostarMensagemLonga(this,
					"O Nº da venda deve ser informado.");
			return;
		}

		// if (obterValor.isInValidoValorEdit(R.id.chq_edtQtdParcela)) {
		// UtilMensagem.mostarMensagemLonga(this,
		// "A quantidade de parcelas deve ser informada.");
		// return;
		// }

		for (int i = 0; i < pagamentos.size(); i++) {
			if (pagamentos.get(i).getDataPrevistaCompensar() == null) {
				UtilMensagem.mostarMensagemLonga(this, "Data do cheque Nº "
						+ (i + 1) + " inválida.");
				return;
			}
			if (pagamentos.size() != (i + 1)) {
				if (pagamentos
						.get(i)
						.getNumeroCheque()
						.equalsIgnoreCase(
								pagamentos.get(i + 1).getNumeroCheque())) {
					UtilMensagem.mostarMensagemLonga(this, "Número do " + (i+1)
							+ "º cheque igual ao " + (i + 2) + "º.");
					return;
				}
			}
		}

		criarDialogSalvar();
	}

	@Override
	protected void salvar() {
		try {
			for (PagamentoCheque pagamento : pagamentos) {
				pagamento.setStatusVenda(venda.getStatusVenda());
			}
			pagmentoChequeDAO.adicionar(pagamentos);
			UtilMensagem.mostarMensagemLonga(this,
					"Pagamento(s) salvo com sucesso!");

			venda.setPagamentosCheque(new HashSet<PagamentoCheque>(pagamentos));
			// FECHAR A VENDA
			venda.fecharVenda();
			vendaDAO.atualizar(venda.getId(), venda);
			Thread.sleep(1000);
			finish();
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}

	public void eventoCliqueRadioButton(View view) {
		boolean selecionadio = ((RadioButton) view).isChecked();
		RadioButton rdbEnviado = (RadioButton) findViewById(R.id.snv_radioEnviado);
		RadioButton rdbNaoEnviado = (RadioButton) findViewById(R.id.snv_radioNaoEnviado);

		switch (view.getId()) {
		case R.id.snv_radioEnviado:
			if (selecionadio) {
				formaPagamento = EnumFormaPagamento.A_VISTA;
				rdbEnviado.setChecked(true);
				rdbNaoEnviado.setChecked(false);
			}
			break;
		case R.id.snv_radioNaoEnviado:
			if (selecionadio) {
				formaPagamento = EnumFormaPagamento.A_PRAZO;
				rdbEnviado.setChecked(false);
				rdbNaoEnviado.setChecked(true);
			}
			break;
		}
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
		case R.id.mnpgd_voltar:
			finish();
			return true;
		case R.id.mnpgd_detalhamento_venda:
			criarDetalhamentoDeVenda();
			return true;
		case R.id.mnpgd_salvar:
			salvarPagamentoCheque();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Pagamento em cheque")
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

	private void criarDetalhamentoDeVenda() {
		try {
			venda = vendaDAO.carregarVendaCompleta(venda.getId());
			venda.setPagamentosCheque(new HashSet<PagamentoCheque>(pagamentos));
			venda.somarTodosItens();
			criarActivityComParametro(DetalhamentoVenda.class, venda);
		} catch (Exception e) {
			tratarExibirErro(e);
		}
	}
}
