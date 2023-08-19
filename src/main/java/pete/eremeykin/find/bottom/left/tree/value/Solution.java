package pete.eremeykin.find.bottom.left.tree.value;

// https://leetcode.com/problems/find-bottom-left-tree-value/submissions/
import java.util.LinkedList;

//Definition for a binary tree node.
/*public*/ class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

class Solution {
    public int findBottomLeftValue(TreeNode root) {
        LeveledTreeNode curr = new LeveledTreeNode(root, 0);
        LinkedList<LeveledTreeNode> queue = new LinkedList<>();
        queue.add(curr);
        while (!queue.isEmpty()) {
            LeveledTreeNode ln = queue.removeFirst();
            TreeNode n = ln.node;
            if (n.left != null) {
                LeveledTreeNode left = new LeveledTreeNode(n.left, ln.level + 1);
                queue.add(left);
                if (left.level > curr.level) {
                    curr = left;
                }
            }
            if (n.right != null) {
                LeveledTreeNode right = new LeveledTreeNode(n.right, ln.level + 1);
                queue.add(right);
                if (right.level > curr.level && n.left == null) {
                    curr = right;
                }
            }
        }
        return curr.node.val;
    }

    static class LeveledTreeNode {
        final TreeNode node;
        final int level;

        LeveledTreeNode(TreeNode node, int level) {
            this.node = node;
            this.level = level;
        }

    }

}