package br.com.actusrota.dao;

import static br.com.actusrota.tabela.TabelaTelefone.CLIENTE;
import static br.com.actusrota.tabela.TabelaTelefone.DDD;
import static br.com.actusrota.tabela.TabelaTelefone.ID;
import static br.com.actusrota.tabela.TabelaTelefone.NUMERO;
import static br.com.actusrota.tabela.TabelaTelefone.OBSERVACAO;
import static br.com.actusrota.tabela.TabelaTelefone.RAMAL;
import static br.com.actusrota.tabela.TabelaTelefone.TABELA_TELEFONE;
import static br.com.actusrota.tabela.TabelaTelefone.TIPO_TELEFONE;

import java.util.HashSet;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.DBAdapter;
import br.com.actusrota.entidade.Cliente;
import br.com.actusrota.entidade.Telefone;
import br.com.actusrota.enumerador.EnumTipoTelefone;
import br.com.actusrota.tabela.TabelaTelefone;

//https://docs.google.com/document/d/1AmD35XHG6e5PqMaXF7pmjCy9BX0gNJYfxYHmxso43Zc/edit
public class TelefoneDAO extends GenericoDAO<Telefone> {

//	private ClienteDAO clienteDAO;

	public TelefoneDAO(Context context, DBAdapter dbAdapter) {
		super(context, dbAdapter, TABELA_TELEFONE);
	}

	// public TelefoneDAO(Context context) {
	// super(context, TABELA_TELEFONE);
	// }

	@Override
	public ContentValues criarContentValues(Telefone telefone) {
		ContentValues values = new ContentValues();
		values.put(ID, telefone.getId());
		values.put(DDD, telefone.getDdd());
		values.put(NUMERO, telefone.getNumero());
		values.put(OBSERVACAO, telefone.getObservacao());
		values.put(RAMAL, telefone.getRamal());
		values.put(TIPO_TELEFONE, telefone.getTipoTelefone().ordinal());
		values.put(CLIENTE, telefone.getCliente().getId());
		return values;
	}

	// { ID, DDD, RAMAL, NUMERO,
	// TIPO_TELEFONE, OBSERVACAO };
	@Override
	public Telefone montarEntidade(Cursor c) {
		Telefone telefone = new Telefone();
		telefone.setId(c.getLong(0));
		telefone.setDdd(c.getString(1));
		telefone.setRamal(c.getString(2));
		telefone.setNumero(c.getString(3));
		telefone.setTipoTelefone(EnumTipoTelefone.buscarEnum(c.getInt(4)));
		telefone.setObservacao(c.getString(5));
		//telefone.setCliente(clienteDAO.consultarPorId(c.getLong(6)));
		return telefone;
	}

	@Override
	public Telefone consultarPorId(long id) {
		Cursor cs = null;
		Telefone telefone = null;
		try {
			abrirBancoSomenteLeitura();
			cs = consultarPorId(TabelaTelefone.COLUNAS_TELEFONE, id);
			if (cs.moveToFirst())
				telefone = montarEntidade(cs);
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return telefone;
	}

	public Set<Telefone> consultarTelefones(Cliente cliente) {
		Cursor c = null;
		Set<Telefone> telefones = new HashSet<Telefone>();
		try {
			abrirBancoSomenteLeitura();
			c = listar(TabelaTelefone.COLUNAS_TELEFONE, "fk_cliente", cliente
					.getId().toString());

			while (c.moveToNext()) {
				Telefone tel = montarEntidade(c);
				tel.setCliente(cliente);
				telefones.add(tel);
			}
		} finally {
			if (c != null)
				c.close();
			fecharBanco();
		}

		return telefones;
	}

}
