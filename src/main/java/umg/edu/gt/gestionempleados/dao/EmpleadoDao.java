package umg.edu.gt.gestionempleados.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import umg.edu.gt.gestionempleados.Conexion;
import umg.edu.gt.gestionempleados.modelo.Empleado;

public class EmpleadoDao {

    // LISTAR EMPLEADOS
	public List<Empleado> listar() {

	    List<Empleado> empleados = new ArrayList<>();

	    String sql = "SELECT * FROM empleados ORDER BY id";

	    try (Connection conexion = Conexion.conectar();
	         PreparedStatement ps = conexion.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {

	            Empleado empleado = new Empleado();

	            empleado.setId(rs.getInt("id"));
	            empleado.setNombre(rs.getString("nombre"));
	            empleado.setDepartamento(
	                    rs.getString("departamento")
	            );
	            empleado.setSalario(
	                    rs.getDouble("salario")
	            );
	            empleado.setFechaContratacion(
	                    rs.getDate("fecha_contratacion")
	                            .toLocalDate()
	            );
	            empleado.setActivo(
	                    rs.getBoolean("activo")
	            );

	            empleados.add(empleado);
	        }

	    } catch (Exception e) {

	        throw new RuntimeException(
	                "Error al listar empleados: "
	                + e.getMessage()
	        );
	    }

	    return empleados;
	}
    // GUARDAR EMPLEADO
    public void guardar(Empleado empleado) {

        String sql = "INSERT INTO empleados " +
                "(nombre, departamento, salario, fecha_contratacion, activo) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getDepartamento());
            ps.setDouble(3, empleado.getSalario());
            ps.setDate(4,
                    java.sql.Date.valueOf(empleado.getFechaContratacion()));
            ps.setBoolean(5, empleado.isActivo());

            ps.executeUpdate();

            System.out.println("Empleado guardado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al guardar empleado: " + e.getMessage());
        }
    }

    // ACTUALIZAR EMPLEADO
    public void actualizar(Empleado empleado) {

        String sql = "UPDATE empleados SET " +
                "nombre = ?, " +
                "departamento = ?, " +
                "salario = ?, " +
                "fecha_contratacion = ?, " +
                "activo = ? " +
                "WHERE id = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getDepartamento());
            ps.setDouble(3, empleado.getSalario());
            ps.setDate(4,
                    java.sql.Date.valueOf(empleado.getFechaContratacion()));
            ps.setBoolean(5, empleado.isActivo());
            ps.setInt(6, empleado.getId());

            ps.executeUpdate();

            System.out.println("Empleado actualizado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al actualizar empleado: " + e.getMessage());
        }
    }

    // ELIMINAR EMPLEADO
    public void eliminar(int id) {

        String sql = "DELETE FROM empleados WHERE id = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

            System.out.println("Empleado eliminado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al eliminar empleado: " + e.getMessage());
        }
    }

    // BUSCAR POR ID
    public Empleado buscarPorId(int id) {

        String sql = "SELECT * FROM empleados WHERE id = ?";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Empleado empleado = new Empleado();

                    empleado.setId(rs.getInt("id"));
                    empleado.setNombre(rs.getString("nombre"));
                    empleado.setDepartamento(
                            rs.getString("departamento"));
                    empleado.setSalario(rs.getDouble("salario"));

                    LocalDate fecha = rs.getDate("fecha_contratacion")
                            .toLocalDate();

                    empleado.setFechaContratacion(fecha);
                    empleado.setActivo(rs.getBoolean("activo"));

                    return empleado;
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar empleado: " + e.getMessage());
        }

        return null;
    }
}