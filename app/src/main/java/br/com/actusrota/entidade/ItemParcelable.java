package br.com.actusrota.entidade;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemParcelable implements Parcelable {

	private ItemListView item;

	public ItemParcelable(ItemListView item) {
		super();
		this.item = item;
		// this.setItem(item);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(item.getQuantidade());
		out.writeSerializable(item.getProduto());
		out.writeSerializable(item.getValorUnitario());
//		out.writeSerializable(item.getTotalUnitario());
//		out.writeParcelable(item.getProduto(), flags);
	}

	public static final Parcelable.Creator<ItemParcelable> CREATOR = new Parcelable.Creator<ItemParcelable>() {
		public ItemParcelable createFromParcel(Parcel in) {
			return new ItemParcelable(in);
		}

		public ItemParcelable[] newArray(int size) {
			return new ItemParcelable[size];
		}
	};

	private ItemParcelable(Parcel in) {
		int quantidade = in.readInt();
		
		item = new ItemListView();
		item.setQuantidade(quantidade);
		Serializable produto = in.readSerializable();
		item.setProduto((Produto) produto);
		Serializable valor = in.readSerializable();
		item.setValorUnitario((Dinheiro) valor);
//		Serializable total = in.readSerializable();
	}

	public ItemListView getItem() {
		return item;
	}
	
	@Override
	public String toString() {
		return item.toString();
	}
	//
	// public void setItem(ItemListView item) {
	// this.item = item;
	// }

}
