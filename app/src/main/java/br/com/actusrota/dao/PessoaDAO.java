package br.com.actusrota.dao;

import static br.com.actusrota.tabela.TabelaPessoa.COLUNAS_PESSOA;
import static br.com.actusrota.tabela.TabelaPessoa.CPF;
import static br.com.actusrota.tabela.TabelaPessoa.DATA_NASC;
import static br.com.actusrota.tabela.TabelaPessoa.EMAIL;
import static br.com.actusrota.tabela.TabelaPessoa.ID;
import static br.com.actusrota.tabela.TabelaPessoa.NOME;
import static br.com.actusrota.tabela.TabelaPessoa.RG;
import static br.com.actusrota.tabela.TabelaPessoa.TABELA_PESSOA;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.Pessoa;
import br.com.actusrota.util.UtilDates;
import br.com.actusrota.util.UtilMensagem;
import br.com.actusrota.util.UtilString;

public class PessoaDAO extends GenericoDAO<Pessoa> {

	public PessoaDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TABELA_PESSOA);
	}

	public PessoaDAO(Context context) {
		super(context, TABELA_PESSOA);
	}
	
	public void delete(Long idCidade) {
		StringBuilder sql = new StringBuilder();
		sql.append(" delete from pessoa where id in");
		sql.append(" (select p.id from pessoa p ");
		sql.append(" inner join cliente c on c.id = p.id ");
		sql.append(" inner join endereco e on e.id = fk_endereco ");
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

	public Pessoa consultarPorId(long id) {
		Pessoa pessoa = null;
		Cursor cs = null;
		try {
			// if (!isAberto())
			abrirBancoSomenteLeitura();
			cs = consultarPorId(COLUNAS_PESSOA, id);
			if (cs.moveToFirst())
				pessoa = montarEntidade(cs);
		} finally {
			// if (isAberto())
			try {
				if (cs != null)
					cs.close();
				fecharBanco();
			} catch (Throwable e) {
				UtilMensagem.mostarMensagemLonga(contexto,
						"Erro ao fechar banco:" + e.getMessage());
			}
		}

		return pessoa;
	}

	public Pessoa pesquisarPor(String cpf) {
		Cursor c = null;
		Pessoa pessoa = null;
		try {
			abrirBancoSomenteLeitura();
			c = listar(COLUNAS_PESSOA, CPF, UtilString.removerMaskCPF(cpf));
			if (c.moveToFirst())
				pessoa = montarEntidade(c);
		} finally {
			if (c != null)
				c.close();
			fecharBanco();
		}

		return pessoa;
	}

	public List<Pessoa> pesquisarLike(String parametro) {
		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		Cursor c = null;
		try {
			abrirBancoSomenteLeitura();
			c = pesquisarLike(COLUNAS_PESSOA, NOME, parametro);

			while (c.moveToNext()) {
				Pessoa cliente = montarEntidade(c);
				pessoas.add(cliente);
			}
		} finally {
			if (c != null)
				c.close();
			fecharBanco();
		}

		return pessoas;
	}

	public Pessoa montarEntidade(Cursor cs) {
		Pessoa pessoa = new Pessoa();
		pessoa.setId(cs.getLong(0));
		pessoa.setCpf(cs.getString(1));
		pessoa.setNome(cs.getString(2));
		pessoa.setRg(cs.getString(3));
		String data = cs.getString(4);
		if (data != null) {
			Log.i("Data:", data);
			pessoa.setDataNascimento(UtilDates.formatarData(data));
		}
		pessoa.setEmail(cs.getString(5));
	
		return pessoa;
	}

	@Override
	public ContentValues criarContentValues(Pessoa pessoa) {
		ContentValues values = new ContentValues();
		values.put(ID, pessoa.getId());
		values.put(CPF, pessoa.getCpf());
		values.put(NOME, pessoa.getNome());
		values.put(RG, pessoa.getRg());
		if (pessoa.getDataNascimento() != null)
			values.put(DATA_NASC,
					UtilDates.formatarData(pessoa.getDataNascimento()));
		values.put(EMAIL, pessoa.getEmail());
		return values;
	}

}
