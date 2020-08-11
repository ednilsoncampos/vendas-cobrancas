package br.com.actusrota.dao;

import static br.com.actusrota.tabela.TabelaEndereco.BAIRRO;
import static br.com.actusrota.tabela.TabelaEndereco.CEP;
import static br.com.actusrota.tabela.TabelaEndereco.CIDADE;
import static br.com.actusrota.tabela.TabelaEndereco.COLUNAS_ENDERECO;
import static br.com.actusrota.tabela.TabelaEndereco.COMPLEMENTO;
import static br.com.actusrota.tabela.TabelaEndereco.ID;
import static br.com.actusrota.tabela.TabelaEndereco.LOGRADOURO;
import static br.com.actusrota.tabela.TabelaEndereco.NUMERO;
import static br.com.actusrota.tabela.TabelaEndereco.PONTO_REFERENCIA;
import static br.com.actusrota.tabela.TabelaEndereco.TABELA_ENDERECO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.Endereco;
import br.com.actusrota.util.UtilMensagem;

public class EnderecoDAO extends GenericoDAO<Endereco> {
	private CidadeDAO cidadeDAO;

	public EnderecoDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TABELA_ENDERECO);
		cidadeDAO = new CidadeDAO(context, dbAdapter);
	}

	public EnderecoDAO(Context context) {
		super(context, TABELA_ENDERECO);
		cidadeDAO = new CidadeDAO(context);
	}

	public Endereco consultarPorId(long id) {
		Cursor cs = null;
		Endereco endereco = null;
		try {
			abrirBancoSomenteLeitura();
			cs = consultarPorId(COLUNAS_ENDERECO, id);
			if (cs.moveToFirst())
				endereco = montarEntidade(cs);
		} finally {
			if (cs != null)
				cs.close();
			try {
				fecharBanco();
			} catch (Exception e) {
				UtilMensagem.mostarMensagemCurta(contexto, "Erro ao fechar banco Endereço."+e.getMessage());
			}
		}
		return endereco;
	}
	
	public void delete(Long idCidade) {
		StringBuilder sql = new StringBuilder();
		sql.append(" delete from endereco where id in");
		sql.append(" (select e.id from endereco e");
		sql.append(" inner join cidade cid on cid.id = e.fk_cidade ");
		sql.append(" where cid.id = ");
		sql.append(String.valueOf(idCidade));
		sql.append(")");
		try {
			abrir();
			iniciarTransacao();
			deleteAdapter(sql.toString());
			comitarTransacao();
		} finally {
			fecharTransacao();
			fechar();
		}
	}

	public Endereco montarEntidade(Cursor cs) {
		Endereco endereco = new Endereco();
		endereco.setId(cs.getLong(0));
		endereco.setLogradouro(cs.getString(1));
		endereco.setBairro(cs.getString(2));
		endereco.setNumero(cs.getString(3));
		endereco.setCep(cs.getString(4));
		endereco.setComplemento(cs.getString(5));
		endereco.setPontoReferencia(cs.getString(6));
		endereco.setCidade(cidadeDAO.consultarPorId(cs.getLong(7)));
		return endereco;
	}

	@Override
	public ContentValues criarContentValues(Endereco endereco) {
		ContentValues values = new ContentValues();
		values.put(ID, endereco.getId());
		values.put(LOGRADOURO, endereco.getLogradouro());
		values.put(BAIRRO, endereco.getBairro());
		values.put(NUMERO, endereco.getNumero());
		values.put(CEP, endereco.getCep());
		values.put(COMPLEMENTO, endereco.getComplemento());
		values.put(PONTO_REFERENCIA, endereco.getPontoReferencia());
		values.put(CIDADE, endereco.getCidade().getId());
		return values;
	}

	public CidadeDAO getCidadeDAO() {
		return cidadeDAO;
	}

}
