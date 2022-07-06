package wd.lb;

public class CricleLinkTable {
    static class Node{
        private String value;
        private Node next;
        public Node(String value, Node next){
            this.value=value;
            this.next=next;
        }
    }
// 注意：对象的值可以为空指针
//    A->B->C->D->E
//    注意构建链表结点的顺序
    public static void main(String[] args) {
//        链表构建完成
        Node E = new Node("E", null);
        Node D = new Node("D", E);
        Node C = new Node("C", D);
        Node B = new Node("B", C);
        Node A = new Node("A", B);
//        循环打印链表结点
//        遍历
        Node p=A;
        while (p!=null){
            System.out.println(p.value);
            p=p.next;
        }
    }
}
