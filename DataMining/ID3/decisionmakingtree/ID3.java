package decisionmakingtree;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map.Entry;

import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ID3 {
	TreeNode root = null;
	ArrayList<DataNode> testDataNodes;
	ArrayList<String> attributes = new ArrayList<>();// 属性名称集合
	HashMap<String, ArrayList<String>> attRange = new HashMap<>();// 属性取值集合

	ArrayList<String> kinds = new ArrayList<>();

	Document document;

	public ID3() {
		// 构造根节点
		readData();
		root = new TreeNode();
		root.isLeaf = false;
		root.att = "root";
		HashSet<String> ignore = new HashSet<>();
		try {
			root.firstchild = CreatTree(testDataNodes, 0, ignore);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(attributes);
		// System.out.println(testDataNodes);
	}

	private void createXML() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factory.newDocumentBuilder();
		document = db.newDocument();
		Element xmlroot = document.createElement("root");
		document.appendChild(xmlroot);
		TreeNode pos = root.firstchild;

		createNode(pos, xmlroot);

		TransformerFactory tf = TransformerFactory.newInstance();
		try {
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			PrintWriter pw = new PrintWriter(System.out);
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
			System.out.println("生成XML文件成功!");
		} catch (TransformerConfigurationException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		} catch (TransformerException e) {
			System.out.println(e.getMessage());
		}
	}

	private void createNode(TreeNode pos, Element element) {
		while (pos != null) {
			if (!pos.isLeaf) {
				Element e = document.createElement(pos.att);
				element.appendChild(e);
				createNode(pos.firstchild, e);
			} else {
				Element e = document.createElement(pos.att);
				e.appendChild(document.createTextNode(pos.kind));
				element.appendChild(e);
			}
			pos = pos.next;
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
			// 计算TestDataNodes中对该属性的增益
			double entropy = calentropy(attribute, dataNodes);
			// 选取最小的
			if (min > entropy) {
				min = entropy;
				pos = attribute;
			}
		}
		// 将此次划分的属性加入到忽视队列中，以后不再考虑该属性
		HashSet<String> newignore = new HashSet<>(ignore);
		newignore.add(pos);

		// 分组

		// childNodes根据最优属性不同的取值，进行分组的数据
		HashMap<String, ArrayList<DataNode>> childNodes = new HashMap<>();
		attriterator = attRange.get(pos).iterator();
		while (attriterator.hasNext()) {
			childNodes.put(attriterator.next(), new ArrayList<>());
		}
		for (DataNode item : dataNodes) {
			childNodes.get(item.attvalues.get(pos)).add(item);
		}

		// 判断，不是叶子节点就递归
		TreeNode tmproot = new TreeNode();
		TreeNode pNode = tmproot;
		Iterator<Entry<String, ArrayList<DataNode>>> iterator = childNodes.entrySet().iterator();
		//对分类过后的每个子集进行分析，查看是否全部属于同一个类
		while (iterator.hasNext()) {
			Entry<String, ArrayList<DataNode>> entry = iterator.next();
			ArrayList<DataNode> cnodes = entry.getValue();
			if(cnodes.isEmpty())
				continue;
			pNode.next = new TreeNode();
			pNode = pNode.next;
			//子集是否为一类
			if (isAllSame(cnodes)) {
				pNode.isLeaf = true;
				pNode.att = entry.getKey();
				pNode.kind = cnodes.get(0).kind;
			} else {
				if (dep < 5) {//深度不满足要求，继续分类
					pNode.isLeaf = false;
					pNode.att = entry.getKey();
					pNode.firstchild = CreatTree(cnodes, dep + 1, newignore);
				} else {//深度满足要求，找到其中多的作为kind
					pNode.isLeaf = true;
					int kindnum[] = new int[kinds.size()];
					for (int i = 0; i < cnodes.size(); i++) {
						kindnum[kinds.indexOf(cnodes.get(i).kind)]++;
					}
					int max = 0,t = 0;
					for (int i = 0; i < cnodes.size(); i++) {
						if(max<kindnum[i]){
							max = kindnum[i];
							t = i;
						}
					}
					pNode.att = cnodes.get(t).kind;
				}
			}
		}
		return tmproot.next;
	}

	// 判断是否完全分类
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

	/**
	 * 
	 * @param attribute
	 *            根据该属性进行切分
	 * @param dataNodes
	 *            进行切分的数据集
	 * @return entropy系数
	 */
	private double calentropy(String attribute, ArrayList<DataNode> dataNodes) {
		ArrayList<String> al = attRange.get(attribute);

		/*
		 * 存储对某种属性的所有取值的种类的计数 kind1 kind2 kind3 val1 0 0 0 val2 0 0 0 val3 0 0 0
		 * 
		 */
		int[][] num = new int[al.size()][kinds.size()];

		// 初始化计数矩阵
		for (int i = 0; i < num.length; i++)
			for (int j = 0; j < num[0].length; j++)
				num[i][j] = 0;
		// 计算
		// 对每种属性的不同种类计数
		for (int i = 0; i < dataNodes.size(); i++) {
			DataNode item = dataNodes.get(i);
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
			// 对某种属性的某个取值的entropy值
			// System.out.println(al.get(i)+" :"+lineans);
			ans += ((double) sum) / dataNodes.size() * lineans;
		}
		return ans;
	}

	// 读入数据
	/**
	 * 读入Data数据
	 * 改变的变量有 attributes attRange testDataNodes
	 */
	private void readData() {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		Scanner in;

		try {
			fis = new FileInputStream("data.txt");
			bis = new BufferedInputStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		in = new Scanner(bis);

		// 读入属性和属性的取值
		String atts = in.nextLine();
		for (String s : atts.split("\\s")) {
			if (s.length() != 0)
				attributes.add(s.trim());
		}
		// 去除kind属性
		// System.out.println(attributes);
		attributes.remove(attributes.size() - 1);
		// 读入测试数据
		testDataNodes = new ArrayList<>();
		Set<String> k = new HashSet<>();
		while (in.hasNext()) {
			// 临时存放属性值
			HashMap<String, String> tmp = new HashMap<>();
			for (int i = 0; i < attributes.size(); i++) {
				String value = in.next();
				if (!attRange.containsKey(attributes.get(i)))
					attRange.put(attributes.get(i), new ArrayList<>());

				if (!attRange.get(attributes.get(i)).contains(value))
					attRange.get(attributes.get(i)).add(value);
				tmp.put(attributes.get(i), value);
			}
			String kind = in.next();
			testDataNodes.add(new DataNode(kind, tmp));
			k.add(kind);
		}
		kinds.addAll(k);
		in.close();
	}

	public static void main(String[] args) throws ParserConfigurationException {
		ID3 id3 = new ID3();
		System.out.println(id3.root);
		id3.createXML();
	}
}
