package mvc.databaseapp.mvapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class DataAPPMainFrame extends JFrame {
    private final int COLUMN = 7;
    private final List<String> TITLES = Arrays.asList("Sid", "Sname", "Ssex", "Sage", "Sclass", "Sdept", "Saddr");
    private DataModel d_model = new DataModel();
    private Vector<Vector<String>> dataModel = new Vector<Vector<String>>();
    private QueryItem id = new QueryItem("学号：", 10);
    private QueryItem name = new QueryItem("姓名：", 10);
    private QueryItem sex = new QueryItem("性别：", 5);
    private QueryItem2 age = new QueryItem2("年龄自：", "到", 5);
    private QueryItem class_ = new QueryItem("班级：", 5);
    private QueryItem dept = new QueryItem("系别：", 5);
    private QueryItem addr = new QueryItem("地址：", 10);
    private JButton queryBtn = new JButton("查询");
    private JButton saveBtn = new JButton("修改");
    private JButton insertBtn = new JButton("添加");
    private JButton deleteBtn = new JButton("删除");
    private JTextArea textarea = new JTextArea(5, 5);
    private MyTable table;

    // 构造函数，负责创建用户界面
    public DataAPPMainFrame(String title) throws SQLException, ClassNotFoundException {
        super(title);

        Vector<String> titles = new Vector<String>(TITLES);
        table = new MyTable(dataModel, titles);
        table.getColumnModel().getColumn(2).setPreferredWidth(30);
        table.getColumnModel().getColumn(3).setPreferredWidth(30);
        table.getColumnModel().getColumn(5).setPreferredWidth(30);
        table.getColumnModel().getColumn(6).setPreferredWidth(150);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(id);
        controlPanel.add(name);
        controlPanel.add(sex);
        controlPanel.add(age);
        controlPanel.add(class_);
        controlPanel.add(dept);
        controlPanel.add(addr);
        controlPanel.add(queryBtn);
        controlPanel.add(saveBtn);
        controlPanel.add(insertBtn);
        controlPanel.add(deleteBtn);
        controlPanel.setPreferredSize(new Dimension(0, 130));

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        tablePanel.add(table.getTableHeader());
        tablePanel.add(new JScrollPane(table));

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.add(textarea, BorderLayout.NORTH);
        container.add(tablePanel, BorderLayout.CENTER);

        this.add(controlPanel, BorderLayout.NORTH);
        this.add(container, BorderLayout.CENTER);
        this.add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.WEST);
        this.add(Box.createRigidArea(new Dimension(20, 0)), BorderLayout.EAST);
        this.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.SOUTH);

        setActionListener();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        DataAPPMainFrame frame = new DataAPPMainFrame("数据库应用演示");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(750, 500));
        frame.setVisible(true);
        frame.setResizable(false);
    }

    // 程序启动时，需调用该方法连接到数据库
    // 之所以不放在构造函数中，是因为这些操作可能抛出异常，需要单独处理

    private void setActionListener() {
        // 根据指定条件，列出数据库中满足条件的记录
        queryBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> conditions = new ArrayList<String>();
                if (id.isSelected())
                    conditions.add("(Sid = '" + id.getText() + "')");
                if (name.isSelected())
                    conditions.add("(Sname like '" + name.getText() + "')");
                if (sex.isSelected())
                    conditions.add("(Ssex = '" + sex.getText() + "')");
                if (age.isSelected())
                    conditions.add("(Sage >= " + age.getText() + " AND " + "Sage <= " + age.getText2() + ")");
                if (class_.isSelected())
                    conditions.add("(Sclass = '" + class_.getText() + "')");
                if (dept.isSelected())
                    conditions.add("(Sdept = '" + dept.getText() + "')");
                if (addr.isSelected())
                    conditions.add("(Saddr like '" + addr.getText() + "')");

                String queryString = d_model.buildSQLQuery(conditions);
                textarea.setText(queryString);

                dataModel.clear();
//                Statement stmt;
                try {
                    dataModel.addAll(d_model.getQueryResult(queryString));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
//                dataModel.add
                // 更新表格
                table.validate();
                table.updateUI();
            }

        });

        // 根据用户当前选中的单元格，修改数据库中对应记录的对应字段
        saveBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                if (row == -1 || column == 0)
                    return;

                String val = dataModel.get(row).get(column);
                String sid = dataModel.get(row).get(0);
                String cmd = d_model.buildSQLUpdateStatement(column, val, sid);
                textarea.setText(cmd);

//				PreparedStatement ps;
                try {
                    int rs = d_model.exeSQLStatement(cmd);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        // 往数据库中插入一条新的记录
        insertBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String sid = id.getText();
                String sname = name.getText();
                String ssex = sex.getText();
                String sage = age.getText();
                String sclass = class_.getText();
                String sdept = dept.getText();
                String saddr = addr.getText();

                // 在文本框显示 SQL 命令
                String cmd = d_model.buildSQLInsertStatement(sid, sname, ssex, sage, sclass, sdept, saddr);
                textarea.setText(cmd);

//				PreparedStatement ps;
                try {
                    int rs = d_model.exeSQLStatement(cmd);
                    dataModel.add(new Vector<String>(Arrays.asList(sid, sname, ssex, sage, sclass, sdept, saddr)));

                    // 更新表格
                    table.validate();
                    table.updateUI();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        });

        // 将用户当前选中的记录从数据库中删除
        deleteBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                String sid = dataModel.get(row).get(0);
                String sql = d_model.buildSQLDeleteStatement(sid);

                // 在文本框显示 SQL 命令
                textarea.setText(sql);

//				Statement stmt;
                try {
                    int rs = d_model.exeSQLStatement(sql);
                    dataModel.remove(row);

                    // 更新表格
                    table.validate();
                    table.updateUI();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        });
    }

}

/*
 * 查询项目 将复选框、标签、文本框组合成一个组件 对外提供获取文本和选中状态的两个方法
 */
class QueryItem extends JPanel {
    private JCheckBox checkbox;
    private JLabel label;
    private JTextField textfield;

    public QueryItem(String labelText, int textWidth) {
        checkbox = new JCheckBox();
        label = new JLabel(labelText);
        textfield = new JTextField(textWidth);
        this.add(checkbox);
        this.add(label);
        this.add(textfield);
    }

    public boolean isSelected() {
        return checkbox.isSelected();
    }

    public String getText() {
        return textfield.getText();
    }
}

/*
 * 同样是查询项目 这是用于查询年龄范围的组件，包含了两个文本框 因此特殊处理，并增加了获取第二个文本框内容的方法
 */
class QueryItem2 extends QueryItem {
    private JLabel label2;
    private JTextField textfield2;

    public QueryItem2(String labelText, String labelText2, int textWidth) {
        super(labelText, textWidth);
        label2 = new JLabel(labelText2);
        textfield2 = new JTextField(textWidth);
        this.add(label2);
        this.add(textfield2);
    }

    public String getText2() {
        return textfield2.getText();
    }
}

/*
 * 表格组件 重载了 JTable 的 isCellEditable 方法 目的是防止编辑 Sid 字段，禁止修改主键
 */
class MyTable extends JTable {
    public MyTable(Vector data, Vector title) {
        super(data, title);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 0)
            return false;
        else
            return true;
    }
}