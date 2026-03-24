public enum EnemyType {
	SPIDER, SKELETON, ALIEN, MUTANT;
	
	public String toString() {
		switch(this) {
		case SPIDER: return "spider";
		case SKELETON: return "skeleton";
		case ALIEN: return "alien";
		case MUTANT: return "mutant";
		}
		return "n/a";
	}
}
