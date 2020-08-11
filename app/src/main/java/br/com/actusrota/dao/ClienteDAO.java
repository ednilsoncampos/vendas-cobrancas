package br.com.actusrota.dao;

import static br.com.actusrota.tabela.TabelaCliente.COLUNAS_CLIENTE;
import static br.com.actusrota.tabela.TabelaCliente.ENDERECO;
import static br.com.actusrota.tabela.TabelaCliente.ID;
import static br.com.actusrota.tabela.TabelaCliente.INDICE_ENDERECO;
import static br.com.actusrota.tabela.TabelaCliente.INDICE_ID;
import static br.com.actusrota.tabela.TabelaCliente.INDICE_NUMERO_CONTROLE;
import static br.com.actusrota.tabela.TabelaCliente.NUMERO_CONTROLE;
import static br.com.actusrota.tabela.TabelaCliente.SELECT;
import static br.com.actusrota.tabela.TabelaCliente.TABELA_CLIENTE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.actusrota.entidade.Cidade;
import br.com.actusrota.entidade.Cliente;
import br.com.actusrota.entidade.Pessoa;
import br.com.actusrota.entidade.Telefone;

public class ClienteDAO extends GenericoDAO<Cliente> {

	private PessoaDAO pessoaDAO;
	private EnderecoDAO enderecoDAO;
	private TelefoneDAO telefoneDAO;
//	private CidadeDAO cidadeDAO;
	// TODO: melhorar quando possivel, utilizado na tela CidadeActivity no
	// método sincronizarClientes
	private Long idCidade;

	public ClienteDAO(Context context) {
		super(context, TABELA_CLIENTE);
		pessoaDAO = new PessoaDAO(context, getDbAdapter());
		enderecoDAO = new EnderecoDAO(context, getDbAdapter());
		telefoneDAO = new TelefoneDAO(context, getDbAdapter());
//		cidadeDAO = new CidadeDAO(context);
	}

	@Override
	public void adicionarImportacao(Collection<Cliente> clientes) {
		if (clientes != null && !clientes.isEmpty()) {
			if (idCidade != null) {
				//A ORDEM ALTERA O RESULTADO {PESSOA,CLIENTE E ENDEREÇO}
				pessoaDAO.delete(idCidade);
				delete();
				enderecoDAO.delete(idCidade);
			}
			for (Cliente cliente : clientes) {
				adicionarImportacao(cliente);
			}
		}
	}

	public void delete() {
		StringBuilder sql = new StringBuilder();
		sql.append(" delete from cliente where id in");
		sql.append(" (select cliente.id from cliente ");
		sql.append(" inner join endereco e on e.id = fk_endereco ");
		sql.append(" inner join cidade cid on cid.id = e.fk_cidade ");
		sql.append(" where cid.id = ");
		sql.append(String.valueOf(idCidade));
		sql.append(")");
		delete(sql.toString());
	}

	@Override
	public void adicionarImportacao(Cliente cliente) {
		try {
			// enderecoDAO.delete(cliente.getEndereco().getId());
			// pessoaDAO.delete(cliente.getId());
			abrirBanco();
			iniciarTransacao();

			// boolean cadastrado = pessoaDAO.isCadastrado(cliente.getId());
			// if (cadastrado) {
			// pessoaDAO.atualizarSemComitar(cliente.getId(), cliente);
			// } else {
			// pessoaDAO.adicionarSemComitar(cliente);
			// }
			//
			// cadastrado = enderecoDAO
			// .isCadastrado(cliente.getEndereco().getId());
			// if (cadastrado) {
			// enderecoDAO.atualizarSemComitar(cliente.getEndereco().getId(),
			// cliente.getEndereco());
			// } else {
			// enderecoDAO.adicionarSemComitar(cliente.getEndereco());
			// }
			//
			// cadastrado = isCadastrado(cliente.getEndereco().getId());
			// if (cadastrado) {
			// atualizarSemComitar(cliente.getId(),
			// cliente);
			// } else {
			// adicionarSemComitar(cliente);
			// }

//			enderecoDAO.adicionarSemComitar(cliente.getEndereco());
//			pessoaDAO.adicionarSemComitar(cliente);
//			super.adicionarSemComitar(cliente);
			
			enderecoDAO.adicionarSemComitar(cliente.getEndereco());
			pessoaDAO.adicionarSemComitar(cliente);
			super.adicionarSemComitar(cliente);
			
			comitarTransacao();
		} catch (Exception e) {
			System.out.println(cliente); // adicionar log
			throw new RuntimeException(e);
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

	@Override
	public void adicionar(Collection<Cliente> clientes) {
		for (Cliente cliente : clientes) {
			adicionar(cliente);
		}
	}

	@Override
	public void adicionar(Cliente cliente) {
		try {
			abrirBanco();
			iniciarTransacao();

			pessoaDAO.adicionarSemComitar(cliente);
			enderecoDAO.adicionarSemComitar(cliente.getEndereco());
			super.adicionarSemComitar(cliente);
			comitarTransacao();
		} catch (Exception e) {
			System.out.println(cliente); // adicionar log
			throw new RuntimeException(e);
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

	public void adicionarNovo(Cliente cliente) {
		try {
			// abrirBanco();
			// iniciarTransacao();

			// Salva na tabela pessoa
			pessoaDAO.bancoNaoCompartilhado = true;
			pessoaDAO.adicionar(cliente);

			enderecoDAO.bancoNaoCompartilhado = true;
			enderecoDAO.adicionar(cliente.getEndereco());

			Long idEndereco = enderecoDAO.buscarMaxID();
			cliente.setEndereco(enderecoDAO.consultarPorId(idEndereco));

			Long idPessoa = pessoaDAO.buscarMaxID();

			cliente.setId(idPessoa);
			super.adicionar(cliente);

			// if (!isAberto()) {
			// abrirBanco();
			// iniciarTransacao();
			// super.adicionar(cliente);
			// comitarTransacao();
			// } else {
			// super.adicionar(cliente);
			// comitarTransacao();
			// }

			for (Telefone tel : cliente.getTelefones()) {
				tel.getCliente().setId(idPessoa);
			}

			telefoneDAO.bancoNaoCompartilhado = true;
			telefoneDAO.adicionar(cliente.getTelefones());
		} catch (Exception e) {
			System.out.println(cliente); // adicionar log
			throw new RuntimeException(e);
		} finally {
			// fecharTransacao();
			fecharBanco();
		}
	}

	public void adicionarCompleto(Cliente cliente) {
		try {
			// pessoaDAO.adicionar(cliente);
			abrirBanco();
			iniciarTransacao();

			pessoaDAO.adicionarSemComitar(cliente);
			enderecoDAO.adicionarSemComitar(cliente.getEndereco());

			Cidade cidade = enderecoDAO.getCidadeDAO().consultarPorId(
					cliente.getCidade().getId());//enderecoDAO.getCidadeDAO()
			
			if (cidade == null) {
				enderecoDAO.getCidadeDAO().adicionarSemComitar(cliente.getCidade());//enderecoDAO.getCidadeDAO()
			}
			super.adicionarSemComitar(cliente);

			comitarTransacao();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(cliente); // adicionar log
			throw new RuntimeException(e);
		} finally {
			fecharTransacao();
			fecharBanco();
		}
	}

	public List<Cliente> getClientes() {
		Cursor cs = null;
		List<Cliente> clientes = new ArrayList<Cliente>();
		try {
			abrirBancoSomenteLeitura();
			cs = listarTodos(COLUNAS_CLIENTE);

			while (cs.moveToNext()) {
				clientes.add(montarEntidade(cs));
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return clientes;
	}

	public List<Cliente> listarClientesPor(Cidade cidade) {
		Cursor cs = null;
		List<Cliente> clientes = new ArrayList<Cliente>();
		try {
			abrirBancoSomenteLeitura();
			StringBuilder sql = new StringBuilder(SELECT);
			sql.append(" from cliente c ");
			sql.append(" inner join endereco e on e.id = c.fk_endereco ");
			sql.append(" inner join cidade cid on cid.id = e.fk_cidade ");
			sql.append(" where cid.id = ? ");
			cs = listarJoin(sql.toString(), String.valueOf(cidade.getId()));

			while (cs.moveToNext()) {
				Cliente c = montarEntidade(cs);
				clientes.add(c);
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return clientes;
	}

	public List<Cliente> listarClientesPor(long idCidade) {
		Cursor cs = null;
		List<Cliente> clientes = new ArrayList<Cliente>();
		try {
			abrirBancoSomenteLeitura();
			StringBuilder sql = new StringBuilder(SELECT);
			sql.append(" from cliente c ");
			sql.append(" inner join endereco e on e.id = c.fk_endereco ");
			sql.append(" inner join cidade cid on cid.id = e.fk_cidade ");
			// sql.append(" inner join rota r on r.id = cid.fk_rota ");
			sql.append(" where cid.id = ? ");
			cs = listarJoin(sql.toString(), String.valueOf(idCidade));
			while (cs.moveToNext()) {
				// if (cs.moveToNext()) {
				Cliente c = montarEntidade(cs);
				clientes.add(c);
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return clientes;
	}

	@Override
	public Cliente consultarPorId(long id) {
		Cursor cs = null;
		Cliente cliente = null;
		try {
			abrirBancoSomenteLeitura();
			cs = consultarPorId(COLUNAS_CLIENTE, id);
			if (cs.moveToFirst())
				cliente = montarEntidade(cs);
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return cliente;
	}

	public Cliente pesquisarPor(String cpf) {
		Cursor cs = null;
		Cliente cliente = null;
		try {
			abrirBancoSomenteLeitura();
			Pessoa pessoa = pessoaDAO.pesquisarPor(cpf);
			if (pessoa == null)
				return null;

			cliente = new Cliente(pessoa);

			cs = consultarPorId(COLUNAS_CLIENTE, pessoa.getId());
			if (cs.moveToFirst()) {
				montarEntidade(cs, cliente);
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}

		return cliente;
	}

	public Cliente pesquisarPor(long idRota, String cpf) {
		Cursor cs = null;
		Cliente cliente = null;
		try {
			abrirBancoSomenteLeitura();
			StringBuilder sql = new StringBuilder(SELECT);
			sql.append(" from cliente c ");
			sql.append(" inner join pessoa p on p.id = c.id ");
			sql.append(" inner join endereco e on e.id = c.fk_endereco ");
			sql.append(" inner join cidade cid on cid.id = e.fk_cidade ");
			sql.append(" inner join rota r on r.id = cid.fk_rota ");
			sql.append(" where p.cpf = ? and r.id = ? ");
			cs = listarJoin(sql.toString(), cpf, String.valueOf(idRota));

			if (cs.moveToFirst())
				cliente = montarEntidade(cs);
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}

		return cliente;
	}

	public List<Cliente> pesquisarLike(String parametro) {
		// pesquisarLike2(parametro);
		Cursor cs = null;
		List<Cliente> clientes = new ArrayList<Cliente>();
		try {
			abrirBancoSomenteLeitura();
			StringBuilder sql = new StringBuilder(SELECT);
			sql.append(" from cliente c ");
			sql.append(" inner join pessoa p on p.id = c.id ");
			sql.append(" where p.nome like ? ");
			cs = listarJoin(sql.toString(), "%" + parametro + "%");

			while (cs.moveToNext()) {
				Cliente c = montarEntidade(cs);// pessoa já está no join, não
												// precisa pesquisar novamente
				clientes.add(c);
			}
		} finally {
			if (cs != null)
				cs.close();
			fecharBanco();
		}
		return clientes;
	}

	// public List<Cliente> pesquisarLike2(String parametro) {
	//
	// List<Pessoa> pessoas = null;
	// List<Cliente> clientes = new ArrayList<Cliente>();
	// try {
	// pessoas = pessoaDAO.pesquisarLike(parametro);
	// } catch (Exception e) {
	// e.printStackTrace();
	// return clientes;
	// }
	//
	// for (Pessoa pessoa : pessoas) {
	// Cliente cliente = consultarPorId(pessoa.getId());
	// if (cliente != null)
	// clientes.add(cliente);
	// }
	// return clientes;
	// }

	public void montarEntidade(Cursor cs, Cliente cliente) {
		cliente.setNumeroControle(cs.getString(INDICE_NUMERO_CONTROLE));
		cliente.setEndereco(enderecoDAO.consultarPorId(cs
				.getLong(INDICE_ENDERECO)));

		Set<Telefone> telefones = telefoneDAO.consultarTelefones(cliente);
		cliente.setTelefones(telefones);

		// long fkTelefone = cs.getLong(3);
		// if (fkTelefone != 0) {
		// cliente.setTelefone(telefoneDAO.consultarPorId(fkTelefone));
		// Set<Telefone> telefones = new HashSet<Telefone>(1);
		// telefones.add(cliente.getTelefone());
		// cliente.setTelefones(telefones);
		// }
	}

	// Erro?? apenas no aparelho ao setar pessoa em cliente
	// O erro ocorria pq ao importar a venda o cliente não foi cadastrado
	public Cliente montarEntidade(Cursor cs) {
		Pessoa pessoa = pessoaDAO.consultarPorId(cs.getLong(INDICE_ID));

		if (pessoa == null)
			throw new RuntimeException("Pessoa não cadastrada para o cliente:"
					+ cs.getLong(INDICE_ID));

		Cliente cliente = new Cliente(pessoa);
		cliente.setEndereco(enderecoDAO.consultarPorId(cs
				.getLong(INDICE_ENDERECO)));
		cliente.setNumeroControle(cs.getString(INDICE_NUMERO_CONTROLE));

		Set<Telefone> telefones = telefoneDAO.consultarTelefones(cliente);
		cliente.setTelefones(telefones);
		// if (cs.getLong(3) != 0) {
		// cliente.setTelefone(telefoneDAO.consultarPorId(cs.getLong(3)));
		// Set<Telefone> telefones = new HashSet<Telefone>(1);
		// telefones.add(cliente.getTelefone());
		// cliente.setTelefones(telefones);
		// }
		return cliente;
	}

	@Override
	public ContentValues criarContentValues(Cliente cliente) {
		ContentValues values = new ContentValues();
		values.put(ID, cliente.getId());
		values.put(NUMERO_CONTROLE, cliente.getNumeroControle());
		values.put(ENDERECO, cliente.getEndereco().getId());
		return values;
	}

	public Long getIdCidade() {
		return idCidade;
	}

	public void setIdCidade(Long idCidade) {
		this.idCidade = idCidade;
	}

}
