package br.com.actusrota.dao;

import static br.com.actusrota.tabela.TabelaAcertoCliente.COLUNAS_ACERTO_CLIENTE;
import static br.com.actusrota.tabela.TabelaAcertoCliente.ID;
import static br.com.actusrota.tabela.TabelaAcertoCliente.LIMITE_PERCENTUAL;
import static br.com.actusrota.tabela.TabelaAcertoCliente.MARGEM_PERCENTUAL;
import static br.com.actusrota.tabela.TabelaAcertoCliente.OPERACAO;
import static br.com.actusrota.tabela.TabelaAcertoCliente.SELECT;
import static br.com.actusrota.tabela.TabelaAcertoCliente.TABELA_ACERTO_CLIENTE;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.AcertoClienteParametro;
import br.com.actusrota.enumerador.EnumOperacao;
import br.com.actusrota.util.UtilMensagem;

public class AcertoClienteParametroDAO extends GenericoDAO<AcertoClienteParametro> {

	public AcertoClienteParametroDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TABELA_ACERTO_CLIENTE);
	}

	public AcertoClienteParametroDAO(Context context) {
		super(context, TABELA_ACERTO_CLIENTE);
	}

	@Override
	public ContentValues criarContentValues(AcertoClienteParametro acerto) {
		ContentValues values = new ContentValues();
		values.put(ID, acerto.getId());
		values.put(LIMITE_PERCENTUAL, acerto.getLimitePercentual());
		values.put(MARGEM_PERCENTUAL, acerto.getMargemPercentual());
		values.put(OPERACAO, acerto.getOperacao().ordinal());
		return values;
	}

	@Override
	public AcertoClienteParametro montarEntidade(Cursor c) {
		AcertoClienteParametro acerto = new AcertoClienteParametro();
		acerto.setId(c.getLong(0));
		acerto.setLimitePercentual(c.getInt(1));
		acerto.setMargemPercentual(c.getInt(2));
		acerto.setOperacao(EnumOperacao.getEnumOperacao(c.getInt(3)));
		return acerto;
	}

	@Override
	public AcertoClienteParametro consultarPorId(long id) {
		Cursor cs = null;
		AcertoClienteParametro acerto = null;
		try {
			abrirBancoSomenteLeitura();
			cs = consultarPorId(COLUNAS_ACERTO_CLIENTE, id);
			if (cs.moveToFirst())
				acerto = montarEntidade(cs);
		} finally {
			if (cs != null)
				cs.close();
			try {
				fecharBanco();
			} catch (Exception e) {
				UtilMensagem.mostarMensagemCurta(contexto, "Erro ao fechar banco AcertoCliente."+e.getMessage());
			}
		}
		return acerto;
	}
	
	public AcertoClienteParametro buscar(EnumOperacao operacao) {
		Cursor cs = null;
		AcertoClienteParametro acerto = null;
		try {
			abrirBancoSomenteLeitura();
			StringBuilder sql = new StringBuilder(SELECT);
			sql.append(" from ");
			sql.append(TABELA_ACERTO_CLIENTE);
			sql.append(" c ");
			sql.append(" where c.operacao = ? ");
			cs = listarJoin(sql.toString(), String.valueOf(operacao.ordinal()));

			if (cs.moveToFirst())
				acerto = montarEntidade(cs);
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}

		return acerto;
	}

}
