package br.com.actusrota;

import java.util.List;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;
import br.com.actusrota.dao.ProdutoDAO;
import br.com.actusrota.entidade.Produto;

public class SearchSuggestionsProvider extends SearchRecentSuggestionsProvider {
	static final String TAG = SearchSuggestionsProvider.class.getSimpleName();
	public static final String AUTHORITY = SearchSuggestionsProvider.class
			.getName();
	public static final int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;
	
	private ProdutoDAO produtoDAO;
	
	private static final String[] COLUMNS = {
			"_id", // must include this column
			SearchManager.SUGGEST_COLUMN_TEXT_1,
			SearchManager.SUGGEST_COLUMN_TEXT_2,
			SearchManager.SUGGEST_COLUMN_INTENT_DATA,
			SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
			SearchManager.SUGGEST_COLUMN_SHORTCUT_ID };

	public SearchSuggestionsProvider() {
		setupSuggestions(AUTHORITY, MODE);
		produtoDAO = new ProdutoDAO(getContext());
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		String query = selectionArgs[0];
		if (query == null || query.length() == 0) {
			return null;
		}

		MatrixCursor cursor = new MatrixCursor(COLUMNS);

		try {
			List<Produto> list = pesquisar(query);
			int n = 0;
			for (Produto obj : list) {
				cursor.addRow(createRow(new Integer(n), query, obj.getCodigo(),
						obj.getDescricao()));
				n++;
			}
		} catch (Exception e) {
			Log.e(TAG, "Failed to lookup " + query, e);
		}
		return cursor;
	}

	private List<Produto> pesquisar(String query) {
		return produtoDAO.pesquisarLike(query);
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}

	private Object[] createRow(Integer id, String text1, String text2,
			String name) {
		return new Object[] { id, // _id
				text1, // text1
				text2, // text2
				text1, "android.intent.action.SEARCH", // action
				SearchManager.SUGGEST_NEVER_MAKE_SHORTCUT };
	}

}
