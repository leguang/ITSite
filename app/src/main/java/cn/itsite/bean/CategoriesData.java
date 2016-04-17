package cn.itsite.bean;

import java.util.ArrayList;

public class CategoriesData {

	public ArrayList<TypeData> TypeDataList;

	public String extend;

	public int code;

	@Override
	public String toString() {
		return "CategoriesData [TypeDataList=" + TypeDataList + ", extend=" + extend + ", code=" + code + "]";
	}

}
