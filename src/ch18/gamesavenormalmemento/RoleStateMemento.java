package ch18.gamesavenormalmemento;

public class RoleStateMemento {
	private int vit;
	private int atk;
	private int def;
	public RoleStateMemento(int vit, int atk, int def) {
		super();
		this.vit = vit;
		this.atk = atk;
		this.def = def;
	}
	public int getVit() {
		return vit;
	}
	public void setVit(int vit) {
		this.vit = vit;
	}
	public int getAtk() {
		return atk;
	}
	public void setAtk(int atk) {
		this.atk = atk;
	}
	public int getDef() {
		return def;
	}
	public void setDef(int def) {
		this.def = def;
	}

}
