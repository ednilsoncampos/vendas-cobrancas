package br.com.actusrota.util;

import java.util.ArrayList;
import java.util.List;

import br.com.actusrota.entidade.IEntidade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class EntidadeCastJson {
	private static final String FORMATO = "dd/MM/yyyy";

	/**
	 * 
	 * @param json
	 * @param classeJson
	 *            Classe que será desserializada
	 * @param tipoAdapter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IEntidade> T jsonToEntidadeAdapter(String json,
			Class<T> classeJson, boolean excluirCamposSerializacao,
			Class<? extends IEntidade>... tipoAdapter) {
		T entidade = null;
		try {

			GsonBuilder gsonBuilder = null;
			if (tipoAdapter == null || tipoAdapter.length == 0) {
				gsonBuilder = criarGsonBuilderAdapter(classeJson);
			} else {
				Object resizeArray = resizeArray(tipoAdapter, tipoAdapter.length+1);
				Class<? extends IEntidade>[] resultado = (Class<? extends IEntidade>[]) resizeArray;
				resultado[tipoAdapter.length] = classeJson;
				gsonBuilder = criarGsonBuilderAdapter(resultado);
			}

			Gson gson = criarGsonAdapter(gsonBuilder, excluirCamposSerializacao);
			entidade = gson.fromJson(json, classeJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entidade;
	}

	/**
	 * 
	 * @param entidade
	 *            Objeto que será serializado
	 * @param tipoAdapter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String entidadeToJsonAdapter(IEntidade entidade,
			boolean excluirCamposSerializacao,
			Class<? extends IEntidade>... tipoAdapter) {
		String json = null;
		try {
			GsonBuilder gsonBuilder = null;
			if (tipoAdapter == null || tipoAdapter.length == 0) {
				gsonBuilder = criarGsonBuilderAdapter(entidade.getClass());
			} else {
				Object resizeArray = resizeArray(tipoAdapter, tipoAdapter.length+1);
				Class<? extends IEntidade>[] resultado = (Class<? extends IEntidade>[]) resizeArray;
				resultado[tipoAdapter.length] = entidade.getClass();
				gsonBuilder = criarGsonBuilderAdapter(resultado);				
			}

			Gson gson = criarGsonAdapter(gsonBuilder, excluirCamposSerializacao);

			json = gson.toJson(entidade);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public static <T extends IEntidade> String listToJson(List<T> dados) {
		String json = null;
		try {
			Gson gson = criarGsonBuilder().create();
			json = gson.toJson(dados);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public static <T extends IEntidade> String listToJsonAdapter(List<T> dados,
			boolean excluirCamposSerializacao,
			Class<? extends IEntidade>... tipoAdapter) {
		String json = null;
		try {

			GsonBuilder gsonBuilder = criarGsonBuilderAdapter(tipoAdapter);

			Gson gson = criarGsonAdapter(gsonBuilder, excluirCamposSerializacao);

			json = gson.toJson(dados);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public static <T extends IEntidade> List<T> jsonToListAdapter(String json,
			Class<T> classeJson, boolean excluirCamposSerializacao,
			Class<? extends IEntidade>... tipoAdapter) {
		List<T> dados = new ArrayList<T>();
		try {
			JsonParser parser = new JsonParser();
			JsonArray array = parser.parse(json).getAsJsonArray();

			GsonBuilder gsonBuilder = null;
			if (tipoAdapter == null || tipoAdapter.length == 0) {
				gsonBuilder = criarGsonBuilderAdapter(classeJson);
			} else {
				Object resizeArray = resizeArray(tipoAdapter, tipoAdapter.length+1);
				Class<? extends IEntidade>[] resultado = (Class<? extends IEntidade>[]) resizeArray;
				resultado[tipoAdapter.length] = classeJson;
				gsonBuilder = criarGsonBuilderAdapter(resultado);
			}

			Gson gson = criarGsonAdapter(gsonBuilder, excluirCamposSerializacao);

			for (int i = 0; i < array.size(); i++) {
				JsonElement jsonElement = array.get(i);
				T entidade = gson.fromJson(jsonElement, classeJson);
				dados.add(entidade);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dados;
	}

	public static <T extends IEntidade> List<T> jsonToList(String json,
			Class<T> classe) {
		List<T> dados = new ArrayList<T>();
		try {
			JsonParser parser = new JsonParser();
			JsonArray array = parser.parse(json).getAsJsonArray();

			Gson gson = criarGsonBuilder().create();

			for (int i = 0; i < array.size(); i++) {
				T entidade = gson.fromJson(array.get(i), classe);
				dados.add(entidade);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dados;
	}

	public static <T extends IEntidade> String entidadeToJson(T entidade) {
		String json = null;
		try {
			Gson gson = criarGsonBuilder().create();
			json = gson.toJson(entidade);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public static <T extends IEntidade> T jsonToEntidade(String json,
			Class<T> classe) {
		T entidade = null;
		try {
			Gson gson = criarGsonBuilder().create();
			entidade = gson.fromJson(json, classe);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entidade;
	}

	private static GsonBuilder criarGsonBuilder() {
		GsonBuilder gBuilder = new GsonBuilder();
		gBuilder.setDateFormat(FORMATO);
		return gBuilder;
	}

	private static Gson criarGsonAdapter(GsonBuilder gsonBuilder,
			boolean excluirCamposSerializacao) throws SecurityException,
			NoSuchFieldException, ClassNotFoundException {
		if (excluirCamposSerializacao)
			gsonBuilder.setExclusionStrategies(new JsonExclusionEstrategy());

		return gsonBuilder.create();
	}

	private static GsonBuilder criarGsonBuilderAdapter(
			Class<? extends IEntidade>... classe) {
		GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();

		GraphAdapterBuilder adapter = new GraphAdapterBuilder();
		for (int i = 0; i < classe.length; i++) {
			adapter.addType(classe[i]);
		}

		adapter.registerOn(gsonBuilder);

		gsonBuilder.setDateFormat(FORMATO);
		return gsonBuilder;
	}
	
	private static Object resizeArray (Object oldArray, int newSize) {
		   int oldSize = java.lang.reflect.Array.getLength(oldArray);
		   Class elementType = oldArray.getClass().getComponentType();
		   Object newArray = java.lang.reflect.Array.newInstance(
		         elementType,newSize);
		   int preserveLength = Math.min(oldSize,newSize);
		   if (preserveLength > 0)
		      System.arraycopy(oldArray,0,newArray,0,preserveLength);

		   return newArray; 
		}
}
