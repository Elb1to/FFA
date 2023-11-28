package io.github.thatkawaiisam.assemble;

import lombok.Getter;

@Getter
public enum AssembleStyle {

	KOHI(true, 15), VIPER(true, -1), MODERN(false, 1), CUSTOM(false, 0);

	private boolean descending;
	private int startNumber;

	AssembleStyle(boolean descending, int startNumber) {
		this.descending = descending;
		this.startNumber = startNumber;
	}

	public AssembleStyle reverse() {
		return descending(!this.descending);
	}

	public AssembleStyle descending(boolean descending) {
		this.descending = descending;
		return this;
	}

	public AssembleStyle startNumber(int startNumber) {
		this.startNumber = startNumber;
		return this;
	}
}
