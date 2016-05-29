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
	ArrayList<String> attributes = new ArrayList<>();// �������Ƽ���
	HashMap<String, ArrayList<String>> attRange = new HashMap<>();// ����ȡֵ����

	ArrayList<String> kinds = new ArrayList<>();

	Document document;

	public ID3() {
		// ������ڵ�
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
			System.out.println("����XML�ļ��ɹ�!");
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

		// ��ÿ�����Խ����������
		while (attriterator.hasNext()) {
			// �õ����Ե�ȡֵ
			String attribute = attriterator.next();
			if (ignore.contains(attribute)) {
				continue;
			}
			// ����TestDataNodes�жԸ����Ե�����
			double entropy = calentropy(attribute, dataNodes);
			// ѡȡ��С��
			if (min > entropy) {
				min = entropy;
				pos = attribute;
			}
		}
		// ���˴λ��ֵ����Լ��뵽���Ӷ����У��Ժ��ٿ��Ǹ�����
		HashSet<String> newignore = new HashSet<>(ignore);
		newignore.add(pos);

		// ����

		// childNodes�����������Բ�ͬ��ȡֵ�����з��������
		HashMap<String, ArrayList<DataNode>> childNodes = new HashMap<>();
		attriterator = attRange.get(pos).iterator();
		while (attriterator.hasNext()) {
			childNodes.put(attriterator.next(), new ArrayList<>());
		}
		for (DataNode item : dataNodes) {
			childNodes.get(item.attvalues.get(pos)).add(item);
		}

		// �жϣ�����Ҷ�ӽڵ�͵ݹ�
		TreeNode tmproot = new TreeNode();
		TreeNode pNode = tmproot;
		Iterator<Entry<String, ArrayList<DataNode>>> iterator = childNodes.entrySet().iterator();
		//�Է�������ÿ���Ӽ����з������鿴�Ƿ�ȫ������ͬһ����
		while (iterator.hasNext()) {
			Entry<String, ArrayList<DataNode>> entry = iterator.next();
			ArrayList<DataNode> cnodes = entry.getValue();
			if(cnodes.isEmpty())
				continue;
			pNode.next = new TreeNode();
			pNode = pNode.next;
			//�Ӽ��Ƿ�Ϊһ��
			if (isAllSame(cnodes)) {
				pNode.isLeaf = true;
				pNode.att = entry.getKey();
				pNode.kind = cnodes.get(0).kind;
			} else {
				if (dep < 5) {//��Ȳ�����Ҫ�󣬼�������
					pNode.isLeaf = false;
					pNode.att = entry.getKey();
					pNode.firstchild = CreatTree(cnodes, dep + 1, newignore);
				} else {//�������Ҫ���ҵ����ж����Ϊkind
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

	// �ж��Ƿ���ȫ����
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
	 *            ���ݸ����Խ����з�
	 * @param dataNodes
	 *            �����зֵ����ݼ�
	 * @return entropyϵ��
	 */
	private double calentropy(String attribute, ArrayList<DataNode> dataNodes) {
		ArrayList<String> al = attRange.get(attribute);

		/*
		 * �洢��ĳ�����Ե�����ȡֵ������ļ��� kind1 kind2 kind3 val1 0 0 0 val2 0 0 0 val3 0 0 0
		 * 
		 */
		int[][] num = new int[al.size()][kinds.size()];

		// ��ʼ����������
		for (int i = 0; i < num.length; i++)
			for (int j = 0; j < num[0].length; j++)
				num[i][j] = 0;
		// ����
		// ��ÿ�����ԵĲ�ͬ�������
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
			// ��ĳ�����Ե�ĳ��ȡֵ��entropyֵ
			// System.out.println(al.get(i)+" :"+lineans);
			ans += ((double) sum) / dataNodes.size() * lineans;
		}
		return ans;
	}

	// ��������
	/**
	 * ����Data����
	 * �ı�ı����� attributes attRange testDataNodes
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

		// �������Ժ����Ե�ȡֵ
		String atts = in.nextLine();
		for (String s : atts.split("\\s")) {
			if (s.length() != 0)
				attributes.add(s.trim());
		}
		// ȥ��kind����
		// System.out.println(attributes);
		attributes.remove(attributes.size() - 1);
		// �����������
		testDataNodes = new ArrayList<>();
		Set<String> k = new HashSet<>();
		while (in.hasNext()) {
			// ��ʱ�������ֵ
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
