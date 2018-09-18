package com.nodream.xskj.module.main.information.model;

import com.nodream.xskj.module.main.contact.model.SortModel;

import java.util.Comparator;

public class PinyinComparator implements Comparator<PatientBean> {

	public int compare(PatientBean o1, PatientBean o2) {
		if (o1.getLetters().equals("@")
				|| o2.getLetters().equals("#")) {
			return 1;
		} else if (o1.getLetters().equals("#")
				|| o2.getLetters().equals("@")) {
			return -1;
		} else {
			return o1.getLetters().compareTo(o2.getLetters());
		}
	}

}
