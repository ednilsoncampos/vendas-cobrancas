package br.com.actusrota.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.PagamentoCheque;
import br.com.actusrota.tabela.TabelaPagamentoCheque;
import br.com.actusrota.util.UtilDates;

public class PagmentoChequeDAO extends GenericoDAO<PagamentoCheque> implements
		IDAO<PagamentoCheque> {

	public PagmentoChequeDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TabelaPagamentoCheque.TABELA_PAG_CHEQUE);
	}

	public PagmentoChequeDAO(Context context) {
		super(context, TabelaPagamentoCheque.TABELA_PAG_CHEQUE);
	}

	@Override
	public ContentValues criarContentValues(PagamentoCheque entidade) {
		ContentValues values = new ContentValues();
		values.put(TabelaPagamentoCheque.ID, entidade.getId());
		values.put(TabelaPagamentoCheque.VALOR, entidade.getValor().getValor());

		Date dataPagamento = entidade.getDataPagamento();
		if (dataPagamento != null)
			values.put(TabelaPagamentoCheque.DATA_PAGAMENTO,
					UtilDates.formatarData(dataPagamento));

		values.put(TabelaPagamentoCheque.COMISSAO_PAGA,
				entidade.isComissaoPaga() ? 1 : 0);// pode acontecer comissï¿½o
													// paga no caso de
													// recobranï¿½a
		values.put(TabelaPagamentoCheque.ID_SATATUS_CHEQUE, entidade
				.getIdStatusCheque().toString());

		values.put(TabelaPagamentoCheque.DATA_PREVISTA_PAGAMENTO,
				UtilDates.formatarData(entidade.getDataPrevistaCompensar()));

		values.put(TabelaPagamentoCheque.ID_FORMA_PAGAMENTO, entidade
				.getIdFormaPagamento().toString());

		values.put(TabelaPagamentoCheque.NUMERO_CHEQUE,
				entidade.getNumeroCheque());

		values.put(TabelaPagamentoCheque.ID_WEB, entidade.getIdWeb());

		values.put(TabelaPagamentoCheque.FK_VENDA, entidade.getIdVenda());
		values.put(TabelaPagamentoCheque.FK_VIAGEM, entidade.getIdViagem());
		values.put(TabelaPagamentoCheque.ID_STATUS_VENDA, entidade.getIdStatusVenda().toString());
		return values;
	}

	@Override
	public PagamentoCheque montarEntidade(Cursor cs) {
		PagamentoCheque pagamento = new PagamentoCheque();
		pagamento.setId(cs.getLong(0));
		pagamento.setValor(Dinheiro.valueOf(cs.getLong(1)));

		String string = cs.getString(2);
		if (string != null)
			pagamento.setDataPagamento(UtilDates.formatarData(string));

		/**
		 * pode acontecer comissão paga no caso de recobrança
		 */
		pagamento.setComissaoPaga(cs.getInt(3) == 1 ? true : false);

		pagamento.setIdStatusCheque(cs.getString(4).charAt(0));

		if (cs.getString(5) != null)
			pagamento.setDataPrevistaCompensar(UtilDates.formatarData(cs
					.getString(5)));

		pagamento.setIdFormaPagamento(cs.getString(6).charAt(0));
		pagamento.setNumeroCheque(cs.getString(7));

		pagamento.setIdWeb(cs.getLong(8));

		pagamento.setIdVenda(cs.getLong(9));
		pagamento.setIdViagem(cs.getLong(10));
		pagamento.setIdStatusVenda(cs.getString(11).charAt(0));
		return pagamento;
	}

	/**
	 * Carrega sem o id do pagamento e define o id como id movel
	 * 
	 * @param cs
	 * @return
	 */
	public PagamentoCheque montarEntidadeExportar(Cursor cs) {
		PagamentoCheque pagamento = new PagamentoCheque();
		
		pagamento.setIdMovel(cs.getLong(0));
		
		pagamento.setValor(Dinheiro.valueOf(cs.getLong(1)));

		String dataString = cs.getString(2);
		if (dataString != null)
			pagamento.setDataPagamento(UtilDates.formatarData(dataString));

		/**
		 * pode acontecer comissão paga no caso de recobrança
		 */
		pagamento.setComissaoPaga(cs.getInt(3) == 1 ? true : false);

		pagamento.setIdStatusCheque(cs.getString(4).charAt(0));

		String dtVencimento = cs.getString(5);
		if (dtVencimento != null)
			pagamento.setDataPrevistaCompensar(UtilDates
					.formatarData(dtVencimento));

		pagamento.setIdFormaPagamento(cs.getString(6).charAt(0));
		pagamento.setNumeroCheque(cs.getString(7));

		pagamento.setIdWeb(cs.getLong(8));

		pagamento.setIdVenda(cs.getLong(9));
		pagamento.setIdViagem(cs.getLong(10));
		pagamento.setIdStatusVenda(cs.getString(11).charAt(0));
		return pagamento;
	}

	@Override
	public PagamentoCheque consultarPorId(long id) {
		Cursor cs = null;
		PagamentoCheque pagamento = null;
		try {
			abrirBancoSomenteLeitura();
			cs = consultarPorId(TabelaPagamentoCheque.COLUNAS_PAG_CHEQUE, id);

			if (cs.moveToFirst())
				pagamento = montarEntidade(cs);
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}

		return pagamento;
	}
	
	public PagamentoCheque consultar(String coluna, Object valor)
			throws SQLException {
		Cursor cs = null;
		PagamentoCheque pagamento = null;
		try {
			abrirBancoSomenteLeitura();
			cs = super.consultar(TabelaPagamentoCheque.COLUNAS_PAG_CHEQUE, coluna, valor);

			if (cs.moveToFirst())
				pagamento = montarEntidade(cs);
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}

		return pagamento;
	}

	public Set<PagamentoCheque> buscarPor(final long idVenda) {
		Cursor c = null;
		Set<PagamentoCheque> pagamentos = new HashSet<PagamentoCheque>();
		try {
			c = listar(TabelaPagamentoCheque.TABELA_PAG_CHEQUE,
					TabelaPagamentoCheque.COLUNAS_PAG_CHEQUE,
					TabelaPagamentoCheque.FK_VENDA, String.valueOf(idVenda));

			while (c.moveToNext()) {
				PagamentoCheque pagamentoCheque = montarEntidade(c);
				pagamentos.add(pagamentoCheque);
			}
		} finally {
			if (c != null)
				c.close();
		}

		return pagamentos;
	}

	public Set<PagamentoCheque> buscarParaExportar(long idVenda) {
		Cursor c = null;
		Set<PagamentoCheque> pagamentos = new HashSet<PagamentoCheque>(0);
		try {
			c = listar(TabelaPagamentoCheque.TABELA_PAG_CHEQUE,
					TabelaPagamentoCheque.COLUNAS_PAG_CHEQUE,
					TabelaPagamentoCheque.FK_VENDA, String.valueOf(idVenda));

			while (c.moveToNext()) {
				PagamentoCheque pagamentoCheque = montarEntidadeExportar(c);
				pagamentos.add(pagamentoCheque);
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return pagamentos;
	}
}
