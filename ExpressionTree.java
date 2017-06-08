
import java.lang.Math.*;

class ExpressionTree {

    private String value;
    private ExpressionTree leftChild, rightChild, parent;

    ExpressionTree() {
        value = null;
        leftChild = rightChild = parent = null;
    }

    // Constructor
    /* Arguments: String s: Value to be stored in the node
                  ExpressionTree l, r, p: the left child, right child, and parent of the node to be created      
       Returns: the newly created ExpressionTree               
     */
    ExpressionTree(String s, ExpressionTree l, ExpressionTree r, ExpressionTree p) {
        value = s;
        leftChild = l;
        rightChild = r;
        parent = p;
    }

    /* Basic access methods */
    String getValue() {
        return value;
    }

    ExpressionTree getLeftChild() {
        return leftChild;
    }

    ExpressionTree getRightChild() {
        return rightChild;
    }

    ExpressionTree getParent() {
        return parent;
    }


    /* Basic setting methods */
    void setValue(String o) {
        value = o;
    }

    // sets the left child of this node to n
    void setLeftChild(ExpressionTree n) {
        leftChild = n;
        n.parent = this;
    }

    // sets the right child of this node to n
    void setRightChild(ExpressionTree n) {
        rightChild = n;
        n.parent = this;
    }

    // Returns the root of the tree describing the expression s
    // Watch out: it makes no validity checks whatsoever!
    ExpressionTree(String s) {
        // check if s contains parentheses. If it doesn't, then it's a leaf
        if (s.indexOf("(") == -1) {
            setValue(s);
        } else {  // it's not a leaf

            /* break the string into three parts: the operator, the left operand,
               and the right operand. ***/
            setValue(s.substring(0, s.indexOf("(")));
            // delimit the left operand 2008
            int left = s.indexOf("(") + 1;
            int i = left;
            int parCount = 0;
            // find the comma separating the two operands
            while (parCount >= 0 && !(s.charAt(i) == ',' && parCount == 0)) {
                if (s.charAt(i) == '(') {
                    parCount++;
                }
                if (s.charAt(i) == ')') {
                    parCount--;
                }
                i++;
            }
            int mid = i;
            if (parCount < 0) {
                mid--;
            }

            // recursively build the left subtree
            setLeftChild(new ExpressionTree(s.substring(left, mid)));

            if (parCount == 0) {
                // it is a binary operator
                // find the end of the second operand.F13
                while (!(s.charAt(i) == ')' && parCount == 0)) {
                    if (s.charAt(i) == '(') {
                        parCount++;
                    }
                    if (s.charAt(i) == ')') {
                        parCount--;
                    }
                    i++;
                }
                int right = i;
                setRightChild(new ExpressionTree(s.substring(mid + 1, right)));
            }
        }
    }

    // Returns a copy of the subtree rooted at this node... 2014
    ExpressionTree deepCopy() {
        ExpressionTree n = new ExpressionTree();
        n.setValue(getValue());
        if (getLeftChild() != null) {
            n.setLeftChild(getLeftChild().deepCopy());
        }
        if (getRightChild() != null) {
            n.setRightChild(getRightChild().deepCopy());
        }
        return n;
    }

    // Returns a String describing the subtree rooted at a certain node.
    public String toString() {
        String ret = value;
        if (getLeftChild() == null) {
            return ret;
        } else {
            ret = ret + "(" + getLeftChild().toString();
        }
        if (getRightChild() == null) {
            return ret + ")";
        } else {
            ret = ret + "," + getRightChild().toString();
        }
        ret = ret + ")";
        return ret;
    }

    // Returns the value of the expression rooted at a given node
    // when x has a certain value
    double evaluate(double x) {
        // WRITE YOUR CODE HERE
        //System.out.println(getRightChild());
        //System.out.println(getLeftChild());
        //System.out.println(getValue());
        //System.out.println(getLeftChild());
        //System.out.println(getRightChild().deepCopy());
        //while(getLeftChild().getValue()!=null || getRightChild().getValue()!=null)
        //PLEASE IGNORE ^^
        if (getValue().equals("mult")) {//checks for operator
            return getLeftChild().evaluate(x) * getRightChild().evaluate(x);
        } else if (getValue().equals("minus")) {//checks for operator
            return getLeftChild().evaluate(x) - getRightChild().evaluate(x);//performs related recursive operation and returns it
        } else if (getValue().equals("add")) {//checks for operator
            return getLeftChild().evaluate(x) + getRightChild().evaluate(x);//performs related recursive operation and returns it
        } else if (getValue().equals("sin")) {//checks for operator
            return Math.sin(getLeftChild().evaluate(x));//performs related recursive operation and returns it
        } else if (getValue().equals("cos")) {//checks for operator
            return Math.cos(getLeftChild().evaluate(x));//performs related recursive operation and returns it
        } else if (getValue().equals("exp")) {//checks for operator
            return Math.exp(getLeftChild().evaluate(x));//performs related recursive operation and returns it
        } else if (this.getValue().equals("x")) {
            return x;//x is a double value that we input
        } else {//hits base case
            return Double.parseDouble(this.getValue());//converts string to double to return it
        }
    }

    ExpressionTree differentiate() {
        ExpressionTree ans = new ExpressionTree();//Empty tree for answer
        ExpressionTree copy = this.deepCopy();//copy of the original tree
        // WRITE YOUR CODE HERE
        if (getValue().equals("mult")) {//product rule
            ans.setValue("add");//the plus between f'(x)g(x)+g'(x)f(x)
            ExpressionTree left = new ExpressionTree("mult", null, null, null);//* in f'(x)*g(x)
            ExpressionTree right = new ExpressionTree("mult", null, null, null);//* between g'(x)*f(x)
            left.setLeftChild(copy.getLeftChild().differentiate());//f'(x) in f'(x)*g(x)
            left.setRightChild(copy.getRightChild());//g(x) in f'(x)*g(x)
            right.setRightChild(copy.getRightChild().differentiate());//g'(x) between g'(x)*f(x)
            right.setLeftChild(copy.getLeftChild());//f(x) between g'(x)*f(x)
            ans.setRightChild(right);//attaching the child nodes to the ans root node (add)
            ans.setLeftChild(left);
            return ans;
        } else if (getValue().equals("add")) {//simple differentiation by differentiating both sides
            ans.setValue("add");
            ans.setLeftChild(copy.getLeftChild().differentiate());
            ans.setRightChild(copy.getRightChild().differentiate());
            return ans;
        } else if (getValue().equals("minus")) {//simple differentiation by differentiating both sides
            ans.setValue("minus");
            ans.setLeftChild(copy.getLeftChild().differentiate());
            ans.setRightChild(copy.getRightChild().differentiate());
            return ans;
        } else if (getValue().equals("cos")) {//cos --> -sin
            ExpressionTree copy1 = this.deepCopy();
            ans.setValue("minus");// the - of -sin
            ExpressionTree trig1 = new ExpressionTree("0"); //as 0-sin
            ans.setLeftChild(trig1);//0 is LC
            ExpressionTree trig2 = new ExpressionTree("mult", null, null, null);//a subtree for RC
            copy1.setValue("sin");//the sin in -sin
            trig2.setLeftChild(copy1);//because the values inside brackets remain same
            //System.out.println(trig2);
            trig2.setRightChild(copy1.getLeftChild().differentiate());//derivative_values in derivative_values * sin(values)
            ans.setRightChild(trig2);//Attaching to main tree
            return ans;
        } else if (getValue().equals("sin")) {//sin --> cos
            ExpressionTree copy1 = this.deepCopy();//to keep the inner values same
            ans.setValue("mult");//* in derivative_values * sin(values)
            copy1.setValue("cos");//changing the sin to cos
            ans.setLeftChild(copy1);//attaching to main tree
            ans.setRightChild(copy1.getLeftChild().differentiate());//derivative_values in derivative_values * sin(values)
            return ans;
        } else if (getValue().equals("exp")) {//e^(something)
            ExpressionTree copy1 = this.deepCopy();//to keep the original thing as it is used in the derivative
            ans.setValue("mult");//* in f'(x)*exp(x)
            ans.setLeftChild(copy1);//exp(x) in f'(x)*exp(x)
            //ExpressionTree exp=new ExpressionTree("mult", null, null, null);
            //copy1.setValue("mult");
            ans.setRightChild(copy1.getLeftChild().differentiate());//f'(x) in f'(x)*exp(x)
            //ans.setRightChild(exp);
            return ans;
        } else if (getValue().equals("x")) {//base case
            ans.setValue("1");//derivative of x
            return ans;
        } else {//constant term base case
            ans.setValue("0");//derivative of constant is 0
            return ans;
        }
    }

    public static void main(String args[]) {
        ExpressionTree e = new ExpressionTree("mult(add(2,x),cos(x))");
        System.out.println(e + "\nEvaluate:");
        System.out.println(e.evaluate(1)+"\n");
        System.out.println(e + "\nDerivative:");
        System.out.println(e.differentiate());
    }
}
