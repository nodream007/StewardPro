package com.nodream.xskj.module.main.contact;

import com.nodream.xskj.module.main.contact.model.MedicalStaffBean;
import com.nodream.xskj.module.main.contact.model.SortModel;

import java.util.Comparator;

public class PinyinComparator implements Comparator<MedicalStaffBean> {

	public int compare(MedicalStaffBean o1, MedicalStaffBean o2) {
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
