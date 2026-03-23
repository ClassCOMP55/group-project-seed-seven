public enum EnemyType {
	SPIDER, SKELETON, ALIEN;
	
	public String toString() {
		switch(this) {
		case SPIDER: return "spider";
		case SKELETON: return "skeleton";
		case ALIEN: return "alien";
		}
		return "n/a";
	}
}
