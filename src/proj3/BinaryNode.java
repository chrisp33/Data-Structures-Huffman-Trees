package proj3;
// BinaryNode class; stores a node in a tree.
//
// CONSTRUCTION: with (a) no parameters, or (b) an Object,
//     or (c) an Object, left child, and right child.
//
// *******************PUBLIC OPERATIONS**********************
// int size( )            --> Return size of subtree at node
// int height( )          --> Return height of subtree at node
// void printPostOrder( ) --> Print a postorder tree traversal
// void printInOrder( )   --> Print an inorder tree traversal
// void printPreOrder( )  --> Print a preorder tree traversal
// BinaryNode duplicate( )--> Return a duplicate tree

/**
 * Binary node class with recursive routines to
 * compute size and height.
 */
final class BinaryNode<AnyType>
{
	
	/**
	 * Constructor for null node
	 */
    public BinaryNode( )
    {
        this( null, null, null );
    }
    
    /**
     * Constructor for node with data to be stored in the node and the children for the node
     * @param theElement - the data to be stored in the node
     * @param lt - the left child
     * @param rt - the right child
     */
	public BinaryNode( AnyType theElement, BinaryNode<AnyType> lt, BinaryNode<AnyType> rt )
    {
        element = theElement;
        left    = lt;
        right   = rt;
    }

	/**
     * Return the size of the binary tree rooted at t.
     */
    public static <AnyType> int size( BinaryNode<AnyType> t )
    {
        if( t == null )
            return 0;
        else
            return 1 + size( t.left ) + size( t.right );
    }

    /**
     * Return the height of the binary tree rooted at t.
     */
    public static <AnyType> int height( BinaryNode<AnyType> t )
    {
        if( t == null )
            return -1;
        else
            return 1 + Math.max( height( t.left ), height( t.right ) );
    }

    // Print tree rooted at current node using preorder traversal.
    public void printPreOrder( )
    {
        System.out.println( element );       // Node
        if( left != null )
            left.printPreOrder( );           // Left
        if( right != null )
            right.printPreOrder( );          // Right
    }


    // Print tree rooted at current node using postorder traversal.
    public void printPostOrder( )
    {
        if( left != null )
            left.printPostOrder( );          // Left
        if( right != null )
            right.printPostOrder( );         // Right
        System.out.println( element );       // Node
    }

    // Print tree rooted at current node using inorder traversal.
    public void printInOrder( )
    {
        if( left != null )
            left.printInOrder( );            // Left
        System.out.println( element );       // Node
        if( right != null )
            right.printInOrder( );           // Right
    }


    /**
     * Return a reference to a node that is the root of a
     * duplicate of the binary tree rooted at the current node.
     */
    public BinaryNode<AnyType> duplicate( )
    {
        BinaryNode<AnyType> root = new BinaryNode<AnyType>( element, null, null );

        if( left != null )            // If there's a left subtree
            root.left = left.duplicate( );    // Duplicate; attach
        if( right != null )          // If there's a right subtree
            root.right = right.duplicate( );  // Duplicate; attach
        return root;                      // Return resulting tree
    }
    
    /**
     * Finds the element contained in the node
     * @return the data stored in the node
     */
    public AnyType getElement( )
    {
        return element;
    }
    
    /**
     * Gets the left child of the node
     * @return the left child
     */
    public BinaryNode<AnyType> getLeft( )
    {
        return left;
    }
    
    /**
     * Gets the right child of the node
     * @return the right child
     */
    public BinaryNode<AnyType> getRight( )
    {
        return right;
    }
    
    /**
     * Changes the element in the node
     * @param x - the new data being set into the node
     */
    public void setElement( AnyType x )
    {
        element = x;
    }
    
    /**
     * Change the left child
     * @param t - the new left child node
     */
    public void setLeft( BinaryNode<AnyType> t )
    {
        left = t;
    }
    
    /**
     * Change the right child
     * @param t - the new right child node
     */
    public void setRight( BinaryNode<AnyType> t )
    {
        right = t;
    }

    private AnyType             element;
    private BinaryNode<AnyType> left;
    private BinaryNode<AnyType> right;
	
}
