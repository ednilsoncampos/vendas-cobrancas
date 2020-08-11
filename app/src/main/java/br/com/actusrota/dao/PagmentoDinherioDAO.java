package br.com.actusrota.dao;

import java.util.HashSet;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.Dinheiro;
import br.com.actusrota.entidade.PagamentoCheque;
import br.com.actusrota.entidade.PagamentoEspecie;
import br.com.actusrota.tabela.TabelaPagamentoEspecie;
import br.com.actusrota.util.UtilDates;

public class PagmentoDinherioDAO extends GenericoDAO<PagamentoEspecie>
		implements IDAO<PagamentoEspecie> {

	public PagmentoDinherioDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TabelaPagamentoEspecie.TABELA_PAG_ESPECIE);
	}

	public PagmentoDinherioDAO(Context context) {
		super(context, TabelaPagamentoEspecie.TABELA_PAG_ESPECIE);
	}

	@Override
	public ContentValues criarContentValues(PagamentoEspecie entidade) {
		ContentValues values = new ContentValues();
		values.put(TabelaPagamentoEspecie.ID, entidade.getId());
		values.put(TabelaPagamentoEspecie.VALOR, entidade.getValor().getValor());
		values.put(TabelaPagamentoEspecie.DATA_PAGAMENTO,
				UtilDates.formatarData(entidade.getDataPagamento()));
		values.put(TabelaPagamentoEspecie.COMISSAO_PAGA,
				entidade.isComissaoPaga() ? 1 : 0);// pode acontecer comissï¿½o
													// paga no caso de
													// recobranï¿½a
		values.put(TabelaPagamentoEspecie.ID_WEB, entidade.getIdWeb());
		values.put(TabelaPagamentoEspecie.FK_VENDA, entidade.getIdVenda());
		values.put(TabelaPagamentoEspecie.FK_VIAGEM, entidade.getIdViagem());
		
		String status = entidade.getIdStatusVenda().toString();
		values.put(TabelaPagamentoEspecie.ID_STATUS_VENDA, status);
		return values;
	}

	@Override
	public PagamentoEspecie montarEntidade(Cursor cs) {
		PagamentoEspecie pagamento = new PagamentoEspecie();
		pagamento.setId(cs.getLong(0));
		pagamento.setValor(Dinheiro.valueOf(cs.getLong(1)));

		String date = cs.getString(2);
		if (date != null)
			pagamento.setDataPagamento(UtilDates.formatarData(date));

		/**
		 * pode acontecer comissï¿½o paga no caso de recobranï¿½a
		 */
		pagamento.setComissaoPaga(cs.getInt(3) == 1 ? true : false);

		pagamento.setIdWeb(cs.getLong(4));

		pagamento.setIdVenda(cs.getLong(5));
		pagamento.setIdViagem(cs.getLong(6));
		pagamento.setIdStatusVenda(cs.getString(7).charAt(0));
		return pagamento;
	}

	/**
	 * Carrega sem o id
	 * { ID, VALOR, DATA_PAGAMENTO, COMISSAO_PAGA,
		 ID_WEB, FK_VENDA, FK_VIAGEM, ID_STATUS_VENDA };
	 * @param cs
	 * @return
	 */
	public PagamentoEspecie montarEntidadeExportar(Cursor cs) {
		PagamentoEspecie pagamento = new PagamentoEspecie();
		pagamento.setIdMovel(cs.getLong(0));
		pagamento.setValor(Dinheiro.valueOf(cs.getLong(1)));

		String dataString = cs.getString(2);
		if (dataString != null)
			pagamento.setDataPagamento(UtilDates.formatarData(dataString));

		/**
		 * pode acontecer comissão paga no caso de recobrança
		 */
		pagamento.setComissaoPaga(cs.getInt(3) == 1 ? true : false);

		pagamento.setIdWeb(cs.getLong(4));
		pagamento.setIdVenda(cs.getLong(5));
		pagamento.setIdViagem(cs.getLong(6));
		
		char charAt = cs.getString(7).charAt(0);
		pagamento.setIdStatusVenda(charAt);
		return pagamento;
	}

	@Override
	public PagamentoEspecie consultarPorId(long id) {
		Cursor cs = null;
		PagamentoEspecie pagamento = null;
		try {
			abrirBancoSomenteLeitura();
			cs = consultarPorId(TabelaPagamentoEspecie.COLUNAS_PAG_ESPECIE, id);

			if (cs.moveToFirst())
				pagamento = montarEntidade(cs);
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}

		return pagamento;
	}
	
	public PagamentoEspecie consultar(String coluna, Object valor)
			throws SQLException {
		Cursor cs = null;
		PagamentoEspecie pagamento = null;
		try {
			abrirBancoSomenteLeitura();
			cs = super.consultar(TabelaPagamentoEspecie.COLUNAS_PAG_ESPECIE, coluna, valor);

			if (cs.moveToFirst())
				pagamento = montarEntidade(cs);
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}

		return pagamento;
	}

	public Set<PagamentoEspecie> buscarPor(final long idVenda) {
		Cursor c = null;
		Set<PagamentoEspecie> pagamentos = new HashSet<PagamentoEspecie>();
		try {
			c = listar(TabelaPagamentoEspecie.TABELA_PAG_ESPECIE,
					TabelaPagamentoEspecie.COLUNAS_PAG_ESPECIE,
					TabelaPagamentoEspecie.FK_VENDA, String.valueOf(idVenda));

			while (c.moveToNext()) {
				PagamentoEspecie pagamento = montarEntidade(c);
				pagamentos.add(pagamento);
			}
		} finally {
			if (c != null)
				c.close();
		}

		return pagamentos;
	}

	public Set<PagamentoEspecie> buscarParaExportar(final long idVenda) {
		Cursor c = null;
		Set<PagamentoEspecie> pagamentos = new HashSet<PagamentoEspecie>();
		try {
			c = listar(TabelaPagamentoEspecie.TABELA_PAG_ESPECIE,
					TabelaPagamentoEspecie.COLUNAS_PAG_ESPECIE,
					TabelaPagamentoEspecie.FK_VENDA, String.valueOf(idVenda));

			while (c.moveToNext()) {
				PagamentoEspecie pagamento = montarEntidadeExportar(c);
				pagamentos.add(pagamento);
			}
		} finally {
			if (c != null)
				c.close();
		}

		return pagamentos;
	}
}
