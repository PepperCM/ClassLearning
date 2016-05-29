package decisionmakingtree;

import java.util.HashMap;

public class DataNode {
	String kind = null;
	HashMap<String, String> attvalues = new HashMap<>();// 每个属性的取值

	public DataNode(String k, HashMap<String, String> att) {
		kind = k;
		attvalues = att;
	}

	@Override
	public String toString() {
		return "kind=" + kind + "  attribute" + attvalues + '\n';
	}
}
