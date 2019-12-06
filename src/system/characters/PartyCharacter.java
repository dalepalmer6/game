package system.characters;

public enum PartyCharacter {
	NINTEN("ninten",
			"Ninten",
			"Ninten", 
			"Name this boy."),
	ANA("ana",
			"Ana",
			"Ana",
			"And this girl."),
	LOID("loid",
			"Lloyd", 
			"Lloyd", 
			"Name your friend."),
	TEDDY("teddy",
			"Teddy", 
			"Teddy", 
			"Name this macho guy."),
	FAVFOOD("favfood",
			"Prime Rib",
			"Prime Rib",
			"What's your favorite food?");
	
	public static PartyCharacter getPCById(int id) {
		PartyCharacter character = null;
		switch(id) {
			case 0: character = PartyCharacter.NINTEN; break;
			case 1: character = PartyCharacter.ANA; break;
			case 2: character = PartyCharacter.LOID; break;
			case 3: character = PartyCharacter.TEDDY; break;
			case 4: character = PartyCharacter.FAVFOOD; break;
		}
		return character;
	}
	
	private String id;
	private String nameOfCharacter;
	private String defaultName;
	private String namingDescription;
	
	public String getId() {
		return id;
	}
	
	public String getNameOfCharacter() {
		return nameOfCharacter;
	}

	public void updateNameOfCharacter(String name) {
		this.nameOfCharacter = name;
	}
	
	public String getDefaultName() {
		return defaultName;
	}
	
	public String getNamingDescription() {
		return namingDescription;
	}
	
	private PartyCharacter(String id, String name, String def, String desc) {
		this.id = id;
		this.nameOfCharacter = name;
		this.defaultName = def;
		this.namingDescription = desc;
	}
}
