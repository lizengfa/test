package Design;

/**
 * 单元素枚举实现单例
 * @author lizengfa
 *
 */
public enum Singleton {
	
	uniqueInstance;
	
	public void singletonOperation(){
		System.out.println("我是一个单例");
	}
	
	public static void main(String[] args) {
		Singleton.uniqueInstance.singletonOperation();
	}

}
