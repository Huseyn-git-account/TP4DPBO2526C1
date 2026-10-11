import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PeopleMenu extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunLahirField;
    private JComboBox<String> kategoriComboBox;
    private JComboBox<String> kotaComboBox;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTable peopleTable;

    private ArrayList<Person> listPerson;
    private int selectedIndex = -1;

    public static void main(String[] args) {
        PeopleMenu menu = new PeopleMenu();
        menu.setSize(700, 600);
        menu.setLocationRelativeTo(null);
        menu.setContentPane(menu.mainPanel);
        menu.getContentPane().setBackground(Color.WHITE);
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public PeopleMenu() {
        listPerson = new ArrayList<>();
        populateList();
        peopleTable.setModel(setTable());

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        String[] kategoriData = {"Category", "Mahasiswa", "Dosen", "Staf", "Alumni"};
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        String[] kotaData = {"CITY", "Bandung", "Jakarta", "Surabaya", "Yogyakarta"};
        kotaComboBox.setModel(new DefaultComboBoxModel<>(kotaData));

        deleteButton.setVisible(false);

        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Apakah Anda yakin ingin menghapus data ini?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedIndex = peopleTable.getSelectedRow();
                if (selectedIndex == -1) return;

                String curId = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = peopleTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curTahun = peopleTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curKategori = peopleTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curKota = peopleTable.getModel().getValueAt(selectedIndex, 5).toString();

                idField.setText(curId);
                namaField.setText(curNama);
                tahunLahirField.setText(curTahun);
                kategoriComboBox.setSelectedItem(curKategori);
                kotaComboBox.setSelectedItem(curKota);

                addUpdateButton.setText("Update");
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        Object[] cols = {"No", "ID", "Nama", "Tahun Lahir", "Kategori", "Kota"};
        DefaultTableModel tmp = new DefaultTableModel(null, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int i = 0; i < listPerson.size(); i++) {
            Object[] row = {
                    i + 1,
                    listPerson.get(i).getId(),
                    listPerson.get(i).getNama(),
                    listPerson.get(i).getTahunLahir(),
                    listPerson.get(i).getKategori(),
                    listPerson.get(i).getKota()
            };
            tmp.addRow(row);
        }
        return tmp;
    }

    public void insertData() {
        try {
            String id = idField.getText();
            String nama = namaField.getText();
            int tahun = Integer.parseInt(tahunLahirField.getText());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String kota = kotaComboBox.getSelectedItem().toString();

            listPerson.add(new Person(id, nama, tahun, kategori, kota));
            peopleTable.setModel(setTable());
            clearForm();

            System.out.println("Insert berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData() {
        try {
            String id = idField.getText();
            String nama = namaField.getText();
            int tahun = Integer.parseInt(tahunLahirField.getText());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String kota = kotaComboBox.getSelectedItem().toString();

            Person p = listPerson.get(selectedIndex);
            p.setId(id);
            p.setNama(nama);
            p.setTahunLahir(tahun);
            p.setKategori(kategori);
            p.setKota(kota);

            peopleTable.setModel(setTable());
            clearForm();

            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData() {
        listPerson.remove(selectedIndex);
        peopleTable.setModel(setTable());
        clearForm();

        System.out.println("Delete berhasil");
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    }

    public void clearForm() {
        idField.setText("");
        namaField.setText("");
        tahunLahirField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        kotaComboBox.setSelectedIndex(0);
        addUpdateButton.setText("Add");
        deleteButton.setVisible(false);
        selectedIndex = -1;
        peopleTable.clearSelection();
    }

    private void populateList() {
        listPerson.add(new Person("P001", "Budi Santoso", 2003, "Mahasiswa", "Bandung"));
        listPerson.add(new Person("P002", "Siti Aminah", 1985, "Dosen", "Jakarta"));
        listPerson.add(new Person("P003", "Rian Pratama", 1995, "Staf", "Surabaya"));
        listPerson.add(new Person("P004", "Dewi Lestari", 2001, "Alumni", "Yogyakarta"));
    }
}