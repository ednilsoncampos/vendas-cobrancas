package br.com.actusrota.dao;

import static br.com.actusrota.tabela.TabelaFuncionario.COLUNAS;
import static br.com.actusrota.tabela.TabelaFuncionario.ID;
import static br.com.actusrota.tabela.TabelaFuncionario.ID_TIPO_FUNCIONARIO;
import static br.com.actusrota.tabela.TabelaFuncionario.MATRICULA;
import static br.com.actusrota.tabela.TabelaFuncionario.TABELA_FUNCIONARIO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.Funcionario;
import br.com.actusrota.entidade.Pessoa;
import br.com.actusrota.entidade.Rota;

public class FuncionarioDAO extends GenericoDAO<Funcionario> {

	private PessoaDAO pessoaDAO;

	public FuncionarioDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TABELA_FUNCIONARIO);
		pessoaDAO = new PessoaDAO(context, dbAdapter);
	}

	/**
	 * O DAO de pessoa compartilha a mesma conexao com o banco por meio do
	 * metodo getDbAdapter
	 * 
	 * @param context
	 */
	public FuncionarioDAO(Context context) {
		super(context, TABELA_FUNCIONARIO);
		pessoaDAO = new PessoaDAO(context, getDbAdapter());
	}

	@Override
	public void adicionarImportacao(Funcionario funcionario) {
		try {
			abrirBanco();
			iniciarTransacao();

			pessoaDAO.adicionarSemComitar(funcionario);
			super.adicionarSemComitar(funcionario);

			comitarTransacao();
		} catch (Exception e) {
			System.out.println(funcionario);// adicionar log
			throw new RuntimeException(e);
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

	public void adicionarPesquisarPessoa(Funcionario funcionario) {
		try {
			abrirBanco();
			iniciarTransacao();
			
//			pessoaDAO.abrirBancoSomenteLeitura();

			Pessoa pessoa = pessoaDAO.pesquisarPor(funcionario.getCpf());
			if (pessoa == null) {
				pessoaDAO.adicionarSemComitar(funcionario);
			}
			super.adicionarSemComitar(funcionario);
			comitarTransacao();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

	@Override
	public void adicionar(Funcionario funcionario) {
		try {
			abrirBanco();
			iniciarTransacao();

			pessoaDAO.adicionarSemComitar(funcionario);
			super.adicionarSemComitar(funcionario);

			comitarTransacao();
		} catch (Exception e) {
			System.out.println(funcionario);// adicionar log
			throw new RuntimeException(e);
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

	public List<Funcionario> getFuncionarios() {
		Cursor cs = null;
		List<Funcionario> funcionarios = new ArrayList<Funcionario>();
		try {
			abrirBancoSomenteLeitura();
			cs = listarTodos(COLUNAS);

			while (cs.moveToNext()) {
				funcionarios.add(montarEntidade(cs));
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}

		return funcionarios;
	}

	public Funcionario consultarPorId(long id) {
		Cursor cs = null;
		Funcionario funcionario = null;
		try {
			abrirBancoSomenteLeitura();
			cs = consultarPorId(COLUNAS, id);

			if (cs.moveToFirst())
				funcionario = montarEntidade(cs);
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}

		return funcionario;
	}

	public Funcionario buscarFuncionarioCadastrado() {
		try {
			abrirBancoSomenteLeitura();
			Long id = buscarMaxID();
			return consultarPorId(id);
		} finally {
			fecharBanco();
		}
	}

	public Funcionario pesquisarPo(String cpf) {
		Pessoa pessoa = pessoaDAO.pesquisarPor(cpf);
		if (pessoa == null)
			return null;

		Funcionario funcionario = new Funcionario(pessoa);
		Cursor cs = null;
		try {
			abrirBancoSomenteLeitura();
			cs = consultarPorId(COLUNAS, pessoa.getId());
			if (cs.moveToFirst()) {
				montarEntidade(cs, funcionario);
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}

		return funcionario;
	}

	public Funcionario montarEntidade(Cursor cs) {
		long id = cs.getLong(0);
		Pessoa pessoa = pessoaDAO.consultarPorId(id);
		Funcionario funcionario = new Funcionario(pessoa);
		funcionario.setMatricula(cs.getString(1));
		funcionario.setIdTipoFuncionario(cs.getString(2).charAt(0));
		return funcionario;
	}

	public Funcionario montarEntidade(Cursor cs, Funcionario funcionario) {
		funcionario.setMatricula(cs.getString(1));
		funcionario.setIdTipoFuncionario(cs.getString(2).charAt(0));
		return funcionario;
	}

	@Override
	public ContentValues criarContentValues(Funcionario funcionario) {
		ContentValues values = new ContentValues();
		values.put(ID, funcionario.getId());
		values.put(MATRICULA, funcionario.getMatricula());
		values.put(ID_TIPO_FUNCIONARIO, funcionario.getIdTipoFuncionario()
				.toString());
		return values;
	}

	public PessoaDAO getPessoaDAO() {
		return pessoaDAO;
	}

}
