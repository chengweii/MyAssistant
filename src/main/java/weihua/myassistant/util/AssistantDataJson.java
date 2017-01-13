package weihua.myassistant.util;

import java.util.LinkedHashMap;

public class AssistantDataJson {
	public Node root;

	public static class Node {
		public String text;
		public LinkedHashMap<String, Node> children;
	}
}
