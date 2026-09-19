package umg.edu.gt.gestionempleados;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import umg.edu.gt.gestionempleados.dao.EmpleadoDao;
import umg.edu.gt.gestionempleados.modelo.Empleado;
import umg.edu.gt.gestionempleados.servicio.EmpleadoService;

public class VentanaEmpleados extends JFrame {

    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtDepartamento;
    private JTextField txtSalario;
    private JTextField txtFecha;

    private JCheckBox chkActivo;

    private JButton btnGuardar;
    private JButton btnBuscar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnTotal;

    private JTable tablaEmpleados;
    private DefaultTableModel modeloTabla;

    private EmpleadoDao empleadoDao;
    private EmpleadoService empleadoService;

    private DateTimeFormatter formato =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public VentanaEmpleados() {

        setTitle("Gestión de Empleados");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        empleadoDao = new EmpleadoDao();
        empleadoService = new EmpleadoService();

        crearInterfaz();
        cargarTabla();
    }

    private void crearInterfaz() {

        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(
                new GridBagLayout()
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;

        panelFormulario.add(
                new JLabel("ID:"), gbc
        );

        txtId = new JTextField(10);

        gbc.gridx = 1;

        panelFormulario.add(
                txtId, gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 1;

        panelFormulario.add(
                new JLabel("Nombre completo:"), gbc
        );

        txtNombre = new JTextField(25);

        gbc.gridx = 1;

        panelFormulario.add(
                txtNombre, gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 2;

        panelFormulario.add(
                new JLabel("Departamento:"), gbc
        );

        txtDepartamento = new JTextField(25);

        gbc.gridx = 1;

        panelFormulario.add(
                txtDepartamento, gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 3;

        panelFormulario.add(
                new JLabel("Salario mensual:"), gbc
        );

        txtSalario = new JTextField(15);

        gbc.gridx = 1;

        panelFormulario.add(
                txtSalario, gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 4;

        panelFormulario.add(
                new JLabel("Fecha contratación:"), gbc
        );

        txtFecha = new JTextField(15);

        gbc.gridx = 1;

        panelFormulario.add(
                txtFecha, gbc
        );

        chkActivo = new JCheckBox("Empleado activo");
        chkActivo.setSelected(true);

        gbc.gridx = 1;
        gbc.gridy = 5;

        panelFormulario.add(
                chkActivo, gbc
        );

        JPanel panelBotones = new JPanel();

        btnGuardar = new JButton("Guardar");
        btnBuscar = new JButton("Buscar");
        btnActualizar = new JButton("Actualizar");
        btnTotal = new JButton("Ver Totales");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        

        panelBotones.add(btnGuardar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnTotal);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;

        panelFormulario.add(
                panelBotones, gbc
        );

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Nombre",
                        "Departamento",
                        "Salario",
                        "Fecha contratación",
                        "Estado"
                }, 0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        tablaEmpleados = new JTable(modeloTabla);

        tablaEmpleados.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollTabla =
                new JScrollPane(tablaEmpleados);

        add(panelFormulario, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);

        btnGuardar.addActionListener(
                e -> guardarEmpleado()
        );

        btnBuscar.addActionListener(
                e -> buscarEmpleado()
        );

        btnActualizar.addActionListener(
                e -> actualizarEmpleado()
        );
        
        

        btnEliminar.addActionListener(
                e -> eliminarEmpleado()
        );

        btnLimpiar.addActionListener(
                e -> limpiarCampos()
        );

        tablaEmpleados.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        cargarEmpleadoSeleccionado();
                    }
                });
    }

    private void guardarEmpleado() {

        try {

            String nombre =
                    txtNombre.getText().trim();

            String departamento =
                    txtDepartamento.getText().trim();

            double salario =
                    Double.parseDouble(
                            txtSalario.getText().trim()
                    );

            LocalDate fecha =
                    LocalDate.parse(
                            txtFecha.getText().trim(),
                            formato
                    );

            boolean activo =
                    chkActivo.isSelected();

            Empleado empleado =
                    new Empleado(
                            nombre,
                            departamento,
                            salario,
                            fecha,
                            activo
                    );

            empleadoService.validarEmpleado(
                    empleado
            );

            empleadoDao.guardar(empleado);

            JOptionPane.showMessageDialog(
                    this,
                    "Empleado registrado correctamente."
            );

            limpiarCampos();
            cargarTabla();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "El salario debe ser un número válido."
            );

        } catch (java.time.format.DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "La fecha debe tener el formato:\n"
                    + "yyyy-MM-dd"
            );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al guardar empleado:\n"
                    + e.getMessage()
            );
        }
    }

    private void buscarEmpleado() {

        try {

            int id =
                    Integer.parseInt(
                            txtId.getText().trim()
                    );

            Empleado empleado =
                    empleadoDao.buscarPorId(id);

            if (empleado == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "No se encontró ningún empleado con ese ID."
                );

                return;
            }

            cargarDatosEmpleado(empleado);

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un ID válido."
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al buscar empleado:\n"
                    + e.getMessage()
            );
        }
    }

    private void actualizarEmpleado() {

        try {

            int id =
                    Integer.parseInt(
                            txtId.getText().trim()
                    );

            String nombre =
                    txtNombre.getText().trim();

            String departamento =
                    txtDepartamento.getText().trim();

            double salario =
                    Double.parseDouble(
                            txtSalario.getText().trim()
                    );

            LocalDate fecha =
                    LocalDate.parse(
                            txtFecha.getText().trim(),
                            formato
                    );

            boolean activo =
                    chkActivo.isSelected();

            Empleado empleado =
                    new Empleado(
                            id,
                            nombre,
                            departamento,
                            salario,
                            fecha,
                            activo
                    );

            empleadoService.validarEmpleado(
                    empleado
            );

            Empleado existente =
                    empleadoDao.buscarPorId(id);

            if (existente == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "No existe un empleado con ese ID."
                );

                return;
            }

            empleadoDao.actualizar(empleado);

            JOptionPane.showMessageDialog(
                    this,
                    "Empleado actualizado correctamente."
            );

            limpiarCampos();
            cargarTabla();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "El ID y el salario deben ser números válidos."
            );

        } catch (java.time.format.DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "La fecha debe tener el formato:\n"
                    + "yyyy-MM-dd"
            );

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al actualizar empleado:\n"
                    + e.getMessage()
            );
        }
    }

    private void eliminarEmpleado() {

        try {

            int id =
                    Integer.parseInt(
                            txtId.getText().trim()
                    );

            Empleado empleado =
                    empleadoDao.buscarPorId(id);

            if (empleado == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "No existe un empleado con ese ID."
                );

                return;
            }

            int respuesta =
                    JOptionPane.showConfirmDialog(
                            this,
                            "¿Está seguro de eliminar al empleado?\n\n"
                            + empleado.getNombre(),
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );

            if (respuesta ==
                    JOptionPane.YES_OPTION) {

                empleadoDao.eliminar(id);

                JOptionPane.showMessageDialog(
                        this,
                        "Empleado eliminado correctamente."
                );

                limpiarCampos();
                cargarTabla();
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un ID válido."
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al eliminar empleado:\n"
                    + e.getMessage()
            );
        }
    }

    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        try {

            for (Empleado empleado :
                    empleadoDao.listar()) {

                modeloTabla.addRow(
                        new Object[]{
                                empleado.getId(),
                                empleado.getNombre(),
                                empleado.getDepartamento(),
                                String.format(
                                        "Q%.2f",
                                        empleado.getSalario()
                                ),
                                empleado
                                        .getFechaContratacion()
                                        .format(formato),
                                empleado.isActivo()
                                        ? "Activo"
                                        : "Inactivo"
                        }
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar empleados:\n"
                    + e.getMessage()
            );
        }
    }

    private void cargarEmpleadoSeleccionado() {

        int fila =
                tablaEmpleados.getSelectedRow();

        if (fila == -1) {
            return;
        }

        int id =
                (int) modeloTabla.getValueAt(
                        fila,
                        0
                );

        Empleado empleado =
                empleadoDao.buscarPorId(id);

        if (empleado != null) {

            cargarDatosEmpleado(empleado);
        }
    }

    private void cargarDatosEmpleado(
            Empleado empleado) {

        txtId.setText(
                String.valueOf(
                        empleado.getId()
                )
        );

        txtNombre.setText(
                empleado.getNombre()
        );

        txtDepartamento.setText(
                empleado.getDepartamento()
        );

        txtSalario.setText(
                String.valueOf(
                        empleado.getSalario()
                )
        );

        txtFecha.setText(
                empleado
                        .getFechaContratacion()
                        .format(formato)
        );

        chkActivo.setSelected(
                empleado.isActivo()
        );
    }

    private void limpiarCampos() {

        txtId.setText("");
        txtNombre.setText("");
        txtDepartamento.setText("");
        txtSalario.setText("");
        txtFecha.setText("");

        chkActivo.setSelected(true);

        tablaEmpleados.clearSelection();

        txtNombre.requestFocus();
    }

    public static void main(String[] args) {

        VentanaEmpleados ventana =
                new VentanaEmpleados();

        ventana.setVisible(true);
    }
}