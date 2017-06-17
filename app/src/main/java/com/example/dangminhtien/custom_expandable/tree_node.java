package com.example.dangminhtien.custom_expandable;

public class tree_node {
    private String node_path;
    private String title;

    public tree_node(String node_path, String title) {
        this.node_path = node_path;
        this.title = title;
    }

    public String getNode_path() {
        return node_path;
    }

    public void setNode_path(String node_path) {
            if (node_path.contains("/")) {
                this.node_path = node_path;
            }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
