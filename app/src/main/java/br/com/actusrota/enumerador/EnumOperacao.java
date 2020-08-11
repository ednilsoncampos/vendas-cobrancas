package br.com.actusrota.enumerador;

import java.util.List;
import java.util.Set;

import android.content.Context;
import br.com.actusrota.dao.AcertoClienteParametroDAO;
import br.com.actusrota.dao.ItemBrindeDAO;
import br.com.actusrota.dao.ItemBrindeExtraDAO;
import br.com.actusrota.dao.ItemDevolucaoDAO;
import br.com.actusrota.dao.ItemTrocaDAO;
import br.com.actusrota.entidade.AcertoClienteParametro;
import br.com.actusrota.entidade.ItemBrinde;
import br.com.actusrota.entidade.ItemBrindeExtra;
import br.com.actusrota.entidade.ItemDevolucao;
import br.com.actusrota.entidade.ItemListView;
import br.com.actusrota.entidade.ItemManter;
import br.com.actusrota.entidade.ItemTroca;
import br.com.actusrota.entidade.Venda;

/**
 * 
 * @author EDNILSON
 *
 */
public enum EnumOperacao {

	/**
	 * A ordem de declaração das contanstes nao devem ser alteradas
	 * O ordinal é salvo tanto no web quanto no movel
	 */
	DEVOLUCAO(" Devolução ") {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends ItemManter> T criarItem(ItemListView itemView) {
			ItemDevolucao itemDev = new ItemDevolucao(itemView);
			return (T) itemDev;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends ItemManter> void salvar(Context contexto,
				List<T> itens) {
			ItemDevolucaoDAO itemDevolucaoDAO = new ItemDevolucaoDAO(contexto);
			itemDevolucaoDAO.adicionar((List<ItemDevolucao>) itens);
		}

		@Override
		public <T extends ItemManter> Set<T> buscarItens(Context contexto,
				Venda venda) {
			ItemDevolucaoDAO itemDAO = new ItemDevolucaoDAO(contexto);
			Set<T> itens = null;
			try {
				itemDAO.abrirBancoSomenteLeitura();
				itens = (Set<T>) itemDAO.buscarItens(venda.getId());
			} finally {
				itemDAO.fecharBanco();
			}
			return itens;
		}

		@Override
		public AcertoClienteParametro buscarParametroAcertoCliente(
				Context contexto) {
			AcertoClienteParametro acertoCliente = new AcertoClienteParametroDAO(
					contexto).buscar(this);
			return acertoCliente;
		}
	},
	BRINDE(" Brinde em Produto ") {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends ItemManter> T criarItem(ItemListView itemView) {
			ItemBrinde item = new ItemBrinde(itemView);
			return (T) item;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends ItemManter> void salvar(Context contexto,
				List<T> itens) {
			ItemBrindeDAO itemDAO = new ItemBrindeDAO(contexto);
			itemDAO.adicionar((List<ItemBrinde>) itens);
		}

		@Override
		public <T extends ItemManter> Set<T> buscarItens(Context contexto,
				Venda venda) {
			ItemBrindeDAO itemDAO = new ItemBrindeDAO(contexto);
			Set<T> itens = null;
			try {
				itemDAO.abrirBancoSomenteLeitura();
				itens = (Set<T>) itemDAO.buscarItens(venda.getId());
			} finally {
				itemDAO.fecharBanco();
			}
			return itens;
		}

		@Override
		public AcertoClienteParametro buscarParametroAcertoCliente(
				Context contexto) {
			AcertoClienteParametro acertoCliente = new AcertoClienteParametroDAO(
					contexto).buscar(this);
			return acertoCliente;
		}
	},
	COMISSAO_CLIENTE(" Brinde em Dinheiro ") {
		@Override
		public <T extends ItemManter> T criarItem(ItemListView itemView) {
			throw new RuntimeException("Não suportado");
		}

		@Override
		public <T extends ItemManter> void salvar(Context contexto,
				List<T> itens) {
			throw new RuntimeException("Não suportado");
		}

		@Override
		public <T extends ItemManter> Set<T> buscarItens(Context contexto,
				Venda venda) {
			throw new RuntimeException("Não suportado");
		}

		@Override
		public AcertoClienteParametro buscarParametroAcertoCliente(
				Context contexto) {
			AcertoClienteParametro acertoCliente = new AcertoClienteParametroDAO(
					contexto).buscar(this);
			return acertoCliente;
		}
	},
	BRINDE_EXTRA(" Brinde Extra ") {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends ItemManter> T criarItem(ItemListView itemView) {
			ItemBrindeExtra item = new ItemBrindeExtra(itemView);
			return (T) item;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends ItemManter> void salvar(Context contexto,
				List<T> itens) {
			ItemBrindeExtraDAO itemDAO = new ItemBrindeExtraDAO(contexto);
			itemDAO.adicionar((List<ItemBrindeExtra>) itens);
		}

		@Override
		public <T extends ItemManter> Set<T> buscarItens(Context contexto,
				Venda venda) {
			ItemBrindeExtraDAO itemDAO = new ItemBrindeExtraDAO(contexto);
			Set<T> itens = null;
			try {
				itemDAO.abrirBancoSomenteLeitura();
				itens = (Set<T>) itemDAO.buscarItens(venda.getId());
			} finally {
				itemDAO.fecharBanco();
			}
			return itens;
		}

		@Override
		public AcertoClienteParametro buscarParametroAcertoCliente(
				Context contexto) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	TROCA(" Troca ") {
		@SuppressWarnings("unchecked")
		@Override
		public <T extends ItemManter> T criarItem(ItemListView itemView) {
			ItemTroca item = new ItemTroca(itemView);
			return (T) item;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T extends ItemManter> void salvar(Context contexto,
				List<T> itens) {
			ItemTrocaDAO itemDAO = new ItemTrocaDAO(contexto);
			itemDAO.adicionar((List<ItemTroca>) itens);
		}

		@Override
		public <T extends ItemManter> Set<T> buscarItens(Context contexto,
				Venda venda) {
			ItemTrocaDAO itemDAO = new ItemTrocaDAO(contexto);
			Set<T> itens = null;
			try {
				itemDAO.abrirBancoSomenteLeitura();
				itens = (Set<T>) itemDAO.buscarItens(venda.getId());
			} finally {
				itemDAO.fecharBanco();
			}
			return itens;
		}

		@Override
		public AcertoClienteParametro buscarParametroAcertoCliente(
				Context contexto) {
			// TODO Auto-generated method stub
			return null;
		}
	};

	private final String descricao;

	private EnumOperacao(String descricao) {
		this.descricao = descricao;
	}

	public abstract <T extends ItemManter> T criarItem(ItemListView itemView);

	public abstract <T extends ItemManter> void salvar(Context contexto,
			List<T> itens);

	public abstract <T extends ItemManter> Set<T> buscarItens(Context contexto,
			Venda venda);

	public abstract AcertoClienteParametro buscarParametroAcertoCliente(
			Context contexto);

	public static EnumOperacao getEnumOperacao(int codigo) {
		for (EnumOperacao operacao : values()) {
			if (operacao.ordinal() == codigo) {
				return operacao;
			}
		}
		return null;
	}

	public String getDescricao() {
		return descricao;
	}
}
