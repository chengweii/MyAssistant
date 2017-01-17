package weihua.myassistant.main;

import weihua.myassistant.context.Context;
import weihua.myassistant.request.RequestType;

/**
 * main
 *
 */
public class App {
	public static void main(String[] args) {
		Context assistantContext = new Context();
		System.out.println(assistantContext.backHome());
		System.out.println(assistantContext.getResponse("探索", RequestType.TEXT));
		System.out.println(assistantContext.getResponse("1", RequestType.CHOICE));
		System.out.println(assistantContext.getResponse("27", RequestType.CHOICE));
		System.out.println(assistantContext.getResponse("1", RequestType.CHOICE));
	}
}
