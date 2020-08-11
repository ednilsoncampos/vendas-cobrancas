package br.com.actusrota.dao;

import static br.com.actusrota.tabela.TabelaRota.COLUNAS_ROTA;
import static br.com.actusrota.tabela.TabelaRota.DESCRICAO;
import static br.com.actusrota.tabela.TabelaRota.ID;
import static br.com.actusrota.tabela.TabelaRota.TABELA_ROTA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.Cidade;
import br.com.actusrota.entidade.Rota;

public class RotaDAO extends GenericoDAO<Rota> {

	private FuncionarioDAO funcionarioDAO;

	public RotaDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TABELA_ROTA);
		funcionarioDAO = new FuncionarioDAO(context, dbAdapter);
	}

	public RotaDAO(Context context) {
		super(context, TABELA_ROTA);
		funcionarioDAO = new FuncionarioDAO(context);
	}

	@Override
	public void adicionarImportacao(Collection<Rota> rotas) {
//		Funcionario funcionario = null;
//		try {
//			funcionario = ((List<Rota>) rotas).get(0).getResponsavel();
//			// ******** se não retornar uma pessoa o funcionario deve ser
//			// cadastrado *********
//			funcionarioDAO.abrirBanco();
//			Pessoa pessoa = funcionarioDAO.getPessoaDAO().pesquisarPor(
//					funcionario.getCpf());
//			if (pessoa == null) {
//				funcionarioDAO.adicionarImportacao(funcionario);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		} finally {
//			funcionarioDAO.fecharBanco();
//		}
		super.adicionarImportacao(rotas);
	}

	@Override
	public void adicionar(Collection<Rota> rotas) {
//		Funcionario funcionario = null;
//		try {
////			funcionario = ((List<Rota>) rotas).get(0).getResponsavel();
//			// ******** se não retornar uma pessoa o funcionario deve ser
//			// cadastrado *********
//			funcionarioDAO.abrirBanco();
//			Pessoa pessoa = funcionarioDAO.getPessoaDAO().pesquisarPor(
//					funcionario.getCpf());
//			if (pessoa == null) {
//				funcionarioDAO.adicionar(funcionario);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		} finally {
//			funcionarioDAO.fecharBanco();
//		}
		super.adicionar(rotas);
	}
	
	public List<Rota> getRotas() {
		List<Rota> rotas = new ArrayList<Rota>(0);
		Cursor c = null;
		try {
			abrirBancoSomenteLeitura();
			c = listarTodos(COLUNAS_ROTA);

			while (c.moveToNext()) {
				Rota rota = montarEntidade(c);
				rotas.add(rota);
			}
		} finally {
			if (c != null)
				c.close();
			fecharBanco();
		}

		return rotas;
	}	

//	public List<Rota> getRotas(long idResponsavel) {
//		List<Rota> rotas = new ArrayList<Rota>();
//		Cursor c = null;
//		try {
//			abrirBancoSomenteLeitura();
//			c = listar(COLUNAS_ROTA, RESPONSAVEL, String.valueOf(idResponsavel));
//
//			while (c.moveToNext()) {
//				Rota a = montarEntidade(c);
//				rotas.add(a);
//			}
//		} finally {
//			c.close();
//			fecharBanco();
//		}
//
//		return rotas;
//	}

	@Override
	public Rota consultarPorId(long id) {
		Cursor c = null;
		Rota rota = null;
		try {
			abrirBancoSomenteLeitura();
			c = consultarPorId(COLUNAS_ROTA, id);

			if (c.moveToFirst())
				rota = montarEntidade(c);
		} finally {
			if (c != null)
				c.close();
			fecharBanco();
		}

		return rota;
	}

//	public Rota buscarPor(long idViagem) {
//		Cursor cs = null;
//		Rota rota = null;
//		try {
//			abrirBancoSomenteLeitura();
//			StringBuilder sql = new StringBuilder(
//					" select r.id, r.descricao ");
//			sql.append(" from viagem v ");
//			sql.append(" inner join rota r on r.id = v.fk_rota ");
//			sql.append(" where v.id = ? ");
//			cs = listarJoin(sql.toString(), String.valueOf(idViagem));
//
//			if (cs.moveToFirst()) {
//				rota = montarEntidade(cs);
//			}
//		} finally {
//			if (cs != null)
//				cs.close();
//			fecharBanco();
//		}
//		return rota;
//	}
	
	public Rota buscarPor(Cidade cliente) {
		Cursor cs = null;
		Rota rota = null;
		try {
			abrirBancoSomenteLeitura();
			StringBuilder sql = new StringBuilder(
					" select r.id, r.descricao ");
			sql.append(" from cidade c ");
//			sql.append(" inner join cidade cid on cid.id = c.fk_rota ");
			sql.append(" inner join rota r on r.id = c.fk_rota ");
			sql.append(" where c.id = ? ");
			cs = listarJoin(sql.toString(), String.valueOf(cliente.getId()));

			if (cs.moveToFirst()) {
				rota = montarEntidade(cs);
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return rota;
	}

	public Rota montarEntidade(Cursor cs) {
		Rota rota = new Rota();
		rota.setId(cs.getLong(0));
		rota.setDescricao(cs.getString(1));
//		rota.setResponsavel(funcionarioDAO.consultarPorId(cs.getLong(2)));
		return rota;
	}

	@Override
	public ContentValues criarContentValues(Rota rota) {
		ContentValues values = new ContentValues();
		values.put(ID, rota.getId());
		values.put(DESCRICAO, rota.getDescricao());
//		values.put(RESPONSAVEL, rota.getResponsavel().getId());
		return values;
	}

	public FuncionarioDAO getFuncionarioDAO() {
		return funcionarioDAO;
	}

}
