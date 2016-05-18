package decisionmakingtree;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map.Entry;

import java.util.Scanner;
import java.util.Set;

class TreeNode {
	TreeNode firstchild = null;
	TreeNode next = null;
	boolean isLeaf = false;
	String att;// 如果是叶子节点，att表示的是类别，如果不是叶子节点，att表示区分的属性
	String kind;

	@Override
	public String toString() {
		if (isLeaf)
			return att + " " + kind + " 1";
		else
			return att + " " + 0;
	}
}

class DataNode {
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

public class ID3 {
	TreeNode root = null;
	ArrayList<DataNode> testDataNodes;
	ArrayList<String> attributes = new ArrayList<>();// 属性名称集合
	HashMap<String, ArrayList<String>> attvalues = new HashMap<>();// 属性取值集合

	ArrayList<String> kinds;

	public ID3() {
		readData();
		TreeNode root = new TreeNode(), pos;
		HashSet<String> ignore = new HashSet<>();
		root.firstchild = CreatTree(testDataNodes, 0, ignore);

		Queue<TreeNode> queue1 = new LinkedList<TreeNode>(), queue2 = new LinkedList<>();
		queue1.add(root);
		while (!(queue1.isEmpty() && queue1.isEmpty())) {
			while (!queue1.isEmpty()) {
				pos = queue1.poll();
				while (pos != null) {
					System.out.print(pos + " ");
					if (pos.firstchild != null)
						queue2.add(pos.firstchild);
					pos = pos.next;
				}
				System.out.print("|");
			}
			System.out.println();
			queue1.addAll(queue2);
			queue2.clear();
		}
	}

	private TreeNode CreatTree(ArrayList<DataNode> dataNodes, int dep, HashSet<String> ignore) {

		Iterator<String> attriterator = attributes.iterator();
		String pos = null;
		double min = Double.MAX_VALUE;
		// 对每种属性进行增益计算
		while (attriterator.hasNext()) {
			// 得到属性的取值
			String attribute = attriterator.next();
			if (ignore.contains(attribute)) {
				continue;

			}
			// 计算dataNdes中节点对与该属性的增益
			// System.out.println(attribute);
			double entropy = calentropy(attribute, dataNodes);
			if (min > entropy) {
				min = entropy;
				pos = attribute;
			}
		}
		HashSet<String> newignore = new HashSet<>(ignore);
		newignore.add(pos);

		// 分组
		// childNodes根据最优属性不同的取值，进行分组的数据
		HashMap<String, ArrayList<DataNode>> childNodes = new HashMap<>();
		attriterator = attvalues.get(pos).iterator();
		while (attriterator.hasNext()) {
			childNodes.put(attriterator.next(), new ArrayList<>());
		}
		for (DataNode item : dataNodes) {
			childNodes.get(item.attvalues.get(pos)).add(item);

		}
		// 判断，不是叶子节点就递归
		TreeNode root = new TreeNode();
		TreeNode pNode = root;
		Iterator<Entry<String, ArrayList<DataNode>>> iterator = childNodes.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, ArrayList<DataNode>> entry = iterator.next();
			ArrayList<DataNode> cnodes = entry.getValue();
			if (cnodes.isEmpty())
				continue;
			pNode.next = new TreeNode();
			pNode = pNode.next;
			if (isAllSame(cnodes)) {
				pNode.isLeaf = true;
				pNode.att = entry.getKey();
				pNode.kind = cnodes.get(0).kind;
			} else {
				if (dep < 7) {
					pNode.isLeaf = false;
					pNode.att = entry.getKey();
					pNode.firstchild = CreatTree(cnodes, dep + 1, newignore);
				} else {
					pNode.isLeaf = true;
					pNode.att = cnodes.get(0).kind;
				}
			}
		}
		return root.next;
	}

	private boolean isAllSame(ArrayList<DataNode> dataNodes) {

		if (dataNodes.isEmpty())
			return true;
		if (dataNodes.size() == 1)
			return true;
		DataNode last, recent;
		Iterator<DataNode> iterator = dataNodes.iterator();
		last = iterator.next();
		while (iterator.hasNext()) {
			recent = iterator.next();
			if (recent.kind != last.kind)
				return false;
			last = recent;
		}
		return true;
	}

	private double calentropy(String attribute, ArrayList<DataNode> dataNodes) {

		// System.out.println(attribute);
		ArrayList<String> al = attvalues.get(attribute);
		// System.out.println(al);
		int[][] num = new int[al.size()][kinds.size()];
		for (int i = 0; i < num.length; i++)
			for (int j = 0; j < num[0].length; j++)
				num[i][j] = 0;
		// 计算

		for (int i = 0; i < dataNodes.size(); i++) {
			DataNode item = dataNodes.get(i);
			// System.out.println("attribute:" + attribute);
			// System.out.println("al:"+al);
			// System.out.println(item.attvalues);
			// System.out.println("1:" + item.attvalues.get(attribute));
			// System.out.println("2:" + kinds.indexOf(item.kind));
			// System.out.println("3:" +
			// al.indexOf(item.attvalues.get(attribute)));

			num[al.indexOf(item.attvalues.get(attribute))][kinds.indexOf(item.kind)]++;
		}

		double ans = 0;
		for (int i = 0; i < num.length; i++) {
			int sum = 0;
			double lineans = 0;
			for (int j = 0; j < num[0].length; j++) {
				sum += num[i][j];
			}
			for (int j = 0; j < num[0].length; j++) {
				if (num[i][j] == 0) {

				} else {
					lineans -= ((double) num[i][j]) / sum * Math.log((double) num[i][j] / sum) / Math.log(2);
				}
			}
			// System.out.println("ines :" + lineans);
			ans += ((double) sum) / dataNodes.size() * lineans;
		}

		/*
		 * System.out.println(attribute+":"); for (int i = 0; i < num.length;
		 * i++) { for (int j = 0; j < num[0].length; j++) {
		 * System.out.print(num[i][j] + " "); } System.out.println(); }
		 */
		return ans;
	}

	private void readData() {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		Scanner in;

		// 分别是测试数据大小，属性的多少，属性取值的多少
		int testdatanum, attnum, attvalnum;

		try {
			fis = new FileInputStream("data.txt");
			bis = new BufferedInputStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		in = new Scanner(bis);
		// 读入属性和属性的取值
		attnum = in.nextInt();
		for (int i = 0; i < attnum; i++) {
			String att = in.next();
			attributes.add(att);
			attvalnum = in.nextInt();
			ArrayList<String> tmp = new ArrayList<>();
			tmp.clear();
			for (int j = 0; j < attvalnum; j++) {
				String e = in.next();
				tmp.add(e);
			}
			attvalues.put(att, tmp);
		}
		// 读入测试数据
		testdatanum = in.nextInt();
		testDataNodes = new ArrayList<>();
		Set<String> k = new HashSet<>();
		for (int i = 0; i < testdatanum; i++) {
			HashMap<String, String> attribute = new HashMap<>();
			attribute.clear();
			Iterator<String> iterator = attributes.iterator();
			while (iterator.hasNext()) {
				attribute.put(iterator.next(), in.next());
			}
			String kind = in.next();
			k.add(kind);
			testDataNodes.add(new DataNode(kind, attribute));
		}
		kinds = new ArrayList<>(k);
		in.close();
	}

	public static void main(String[] args) {
		new ID3();
		// System.out.println(id3.testDataNodes);
	}
}
