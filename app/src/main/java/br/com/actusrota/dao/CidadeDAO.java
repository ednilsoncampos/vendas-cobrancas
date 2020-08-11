package br.com.actusrota.dao;

import static br.com.actusrota.tabela.TabelaCidade.COLUNAS_CIDADE;
import static br.com.actusrota.tabela.TabelaCidade.ID;
import static br.com.actusrota.tabela.TabelaCidade.NOME;
import static br.com.actusrota.tabela.TabelaCidade.SIGLA_UF;
import static br.com.actusrota.tabela.TabelaCidade.TABELA_CIDADE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.Cidade;
import br.com.actusrota.entidade.Rota;
import br.com.actusrota.tabela.TabelaCidade;

public class CidadeDAO extends GenericoDAO<Cidade> {
	private RotaDAO rotaDAO;

	public CidadeDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TABELA_CIDADE);
		rotaDAO = new RotaDAO(context, dbAdapter);
	}

	public CidadeDAO(Context context) {
		super(context, TABELA_CIDADE);
		rotaDAO = new RotaDAO(context);
	}

	@Override
	public void adicionarImportacao(Collection<Cidade> dados) {
		try {
			abrirBanco();
			iniciarTransacao();
			for (Cidade cidade : dados) {
				this.adicionarImportacao(cidade);
			}
			comitarTransacao();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

	@Override
	public void adicionarImportacao(Cidade entidade) {
		try {
			if (!cidadeRotaJaCadastrada(entidade.getId(), entidade
					.getRota().getId())) {
				adicionarCidadeRota(entidade.getId(), entidade.getRota()
						.getId());
			}
			// super.adicionarImportacao(entidade);
			if (!isCadastrado(entidade.getId())) {
				super.adicionarSemComitar(entidade);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void adicionarCidadeRota(long idCidade, long idRota) {
		ContentValues values = new ContentValues();
		values.put(TabelaCidade.CIDADE, idCidade);
		values.put(TabelaCidade.ROTA_CIDADE, idRota);
		getDbAdapter().adicionar(TabelaCidade.TABELA_CIDADE_ROTA, values);
	}

	public boolean cidadeRotaJaCadastrada(long idCidade, long idRota) {
		Cursor cs = null;
		boolean cadastrada = false;
		try {
			StringBuilder sql = new StringBuilder(" select cr.fk_cidade ");
			sql.append(" from cidade_rota cr");
			sql.append(" where cr.fk_cidade = ? and cr.fk_rota = ? ");
			cs = listarJoin(sql.toString(), String.valueOf(idCidade),
					String.valueOf(idRota));

			cadastrada = cs.moveToFirst();
		} finally {
			if (cs != null)
				cs.close();
		}
		return cadastrada;
	}

	public List<Cidade> listarCidadesPor(Rota rota) {
		Cursor cs = null;
		List<Cidade> cidades = new ArrayList<Cidade>();
		try {
			abrirBancoSomenteLeitura();
			StringBuilder sql = new StringBuilder(
					" select c.id, c.nome, c.sigla_uf, r.id ");
			sql.append(" from cidade_rota cr");
			sql.append(" inner join cidade c on c.id = cr.fk_cidade");
			sql.append(" inner join rota r on r.id = cr.fk_rota ");
			sql.append(" where r.id = ?");
			cs = listarJoin(sql.toString(), String.valueOf(rota.getId()));

			while (cs.moveToNext()) {
				Cidade c = montarEntidade(cs);
				cidades.add(c);
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return cidades;
	}

	@Override
	public Cidade consultarPorId(long idCidade) {
		Cursor cs = null;
		Cidade cidade = null;
		try {
			abrirBancoSomenteLeitura();
			cs = consultarPorId(COLUNAS_CIDADE, idCidade);
			if (cs.moveToFirst())
				cidade = montarEntidade(cs);
		} finally {
			cs.close();
			fecharBanco();
		}
		return cidade;
	}

	public List<Cidade> pesquisarLikePor(Rota rota, String nome) {
		Cursor cs = null;
		List<Cidade> cidades = new ArrayList<Cidade>();
		try {
			abrirBancoSomenteLeitura();
			StringBuilder sql = new StringBuilder(
					" select c.id, c.nome, c.sigla_uf, r.id ");
			sql.append(" from cidade_rota cr");
			sql.append(" inner join cidade c on c.id = cr.fk_cidade");
			sql.append(" inner join rota r on r.id = cr.fk_rota ");
			sql.append(" where r.id = ? and c.nome like ? ");
			cs = listarJoin(sql.toString(), String.valueOf(rota.getId()), "%"
					+ nome + "%");

			while (cs.moveToNext()) {
				Cidade c = montarEntidade(cs);
				cidades.add(c);
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return cidades;
	}

	/**
	 * Banco já está aberto para o DAO cidade, o DAO da rota deve abrir também
	 */
	public Cidade montarEntidade(Cursor cs) {
		Cidade cidade = new Cidade();
		cidade.setId(cs.getLong(0));
		cidade.setNome(cs.getString(1));
		cidade.setSiglaUF(cs.getString(2));
		// cidade.setRota(rotaDAO.consultarPorId(cs.getLong(3)));
		return cidade;
	}

	@Override
	public ContentValues criarContentValues(Cidade cidade) {
		ContentValues values = new ContentValues();
		values.put(ID, cidade.getId());
		values.put(NOME, cidade.getNome());
		values.put(SIGLA_UF, cidade.getSiglaUF());

		// Rota rota = cidade.getRota();
		// if(rota == null || rota.getId() == null) {
		// throw new RuntimeException("Rota não pode ser nula.");
		// }
		// values.put(ROTA, rota.getId());

		return values;
	}
}
