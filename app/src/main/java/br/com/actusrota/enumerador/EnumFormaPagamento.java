package br.com.actusrota.enumerador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.actusrota.entidade.ContaReceber;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.PagamentoCheque;
import br.com.actusrota.entidade.PagamentoEspecie;
import br.com.actusrota.util.UtilDates;
import br.com.actusrota.util.UtilDinheiro;

public enum EnumFormaPagamento {

	A_VISTA('V', "Cheque a vista") {
		@Override
		public PagamentoCheque criarContaReceber() {
			return criar();
		}

		@Override
		public List<? extends ContaReceber> gerarParcelas(Dinheiro valorTotal,
				int qtdParcelas, Date dataVenda, Object... parametro) {

			return gerarParcelas(this, valorTotal, qtdParcelas, dataVenda,
					parametro);
		}

	},
	A_PRAZO('P', "Cheque a prazo") {
		@Override
		public PagamentoCheque criarContaReceber() {
			return criar();
		}

		@Override
		public List<? extends ContaReceber> gerarParcelas(Dinheiro valorTotal,
				int qtdParcelas, Date dataVenda, Object... parametro) {
			return gerarParcelas(this, valorTotal, qtdParcelas, dataVenda,
					parametro);
		}
	},
	ESPECIE('E', "Dinheiro") {
		@Override
		public PagamentoEspecie criarContaReceber() {
			return new PagamentoEspecie();
		}

		@Override
		public List<? extends ContaReceber> gerarParcelas(Dinheiro valor,
				int qtdParcelas, Date dataVenda, Object... parametro) {
			List<PagamentoEspecie> pagamentos = new ArrayList<PagamentoEspecie>();

			PagamentoEspecie pagamentoEspecie = new PagamentoEspecie();
			pagamentoEspecie.setDataPagamento(dataVenda);
			pagamentoEspecie.setValor(valor);

			pagamentos.add(pagamentoEspecie);
			return pagamentos;
		}
	};

	private final Character id;
	private final String descricao;

	private static List<EnumFormaPagamento> pagamentosCheque;

	static {
		pagamentosCheque = new ArrayList<EnumFormaPagamento>();
		pagamentosCheque.add(A_VISTA);
		pagamentosCheque.add(A_PRAZO);
	}

	private EnumFormaPagamento(Character id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Character getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public static EnumFormaPagamento consultarPorId(Character id) {
		for (EnumFormaPagamento formaPagamento : values()) {
			if (formaPagamento.getId().equals(id))
				return formaPagamento;
		}
		return null;
	}

	private static PagamentoCheque criar() {
		PagamentoCheque pagamento = new PagamentoCheque();
		// pagamento.setCompensado(false);
		return pagamento;
	}

	public abstract ContaReceber criarContaReceber();

	public abstract List<? extends ContaReceber> gerarParcelas(Dinheiro valor,
			int qtdParcelas, Date dataVenda, Object... parametro);

	public static List<EnumFormaPagamento> pagamentosCheque() {
		return pagamentosCheque;
	}

	public static List<EnumFormaPagamento> getFormasPagamento() {
		return Arrays.asList(values());
	}

	List<? extends ContaReceber> gerarParcelas(
			EnumFormaPagamento formaPagamento, Dinheiro valorTotal,
			int qtdParcelas, Date dataVenda, Object... parametro) {
		Dinheiro[] parcelas = UtilDinheiro.parcelar(valorTotal, qtdParcelas);
		List<PagamentoCheque> cheques = new ArrayList<PagamentoCheque>();
		boolean temParametro = (parametro != null && parametro.length > 0);

		for (int i = 1; i < qtdParcelas; i++) {// gera uma parcela a menos
			PagamentoCheque cheque = criar();
			if (temParametro) {
				cheque.setNumeroCheque(parametro[0].toString());
				cheque.setIdVenda(Long.valueOf(parametro[1].toString()));
				cheque.setIdViagem(Long.valueOf(parametro[2].toString()));
//				cheque.setStatusCheque(statusCheque);
			}
			cheque.setFormaPagamento(formaPagamento);
			cheque.setStatusCheque(EnumStatusCheque.EM_COMPENSACAO);
			cheque.setDataPrevistaCompensar(UtilDates
					.adicionarMes(dataVenda, i));
			cheque.setValor(parcelas[0]);

			if (!cheques.contains(cheque))
				cheques.add(cheque);
		}

		if (parcelas[1].isNovo()) {
			PagamentoCheque cheque = criar();

			if (temParametro) {
				cheque.setNumeroCheque(parametro[0].toString());
				cheque.setIdVenda(Long.valueOf(parametro[1].toString()));
				cheque.setIdViagem(Long.valueOf(parametro[2].toString()));
			}
			cheque.setFormaPagamento(formaPagamento);
			cheque.setStatusCheque(EnumStatusCheque.EM_COMPENSACAO);
			cheque.setDataPrevistaCompensar(UtilDates.adicionarMes(dataVenda,
					qtdParcelas));
			cheque.setValor(parcelas[0]);

			if (!cheques.contains(cheque))
				cheques.add(cheque);
		} else {
			PagamentoCheque cheque = criar();

			if (temParametro) {
				cheque.setNumeroCheque(parametro[0].toString());
				cheque.setIdVenda(Long.valueOf(parametro[1].toString()));
				cheque.setIdViagem(Long.valueOf(parametro[2].toString()));
			}
			cheque.setFormaPagamento(formaPagamento);
			cheque.setStatusCheque(EnumStatusCheque.EM_COMPENSACAO);
			cheque.setDataPrevistaCompensar(UtilDates.adicionarMes(dataVenda,
					qtdParcelas));
			cheque.setValor(UtilDinheiro.somar(parcelas[0], parcelas[1]));

			if (!cheques.contains(cheque))
				cheques.add(cheque);
		}
		return cheques;
	}
}
