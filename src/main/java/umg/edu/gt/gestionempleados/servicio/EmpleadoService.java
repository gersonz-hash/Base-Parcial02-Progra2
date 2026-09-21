package umg.edu.gt.gestionempleados.servicio;

import java.time.LocalDate;

import umg.edu.gt.gestionempleados.modelo.Empleado;

public class EmpleadoService {

    public void validarEmpleado(Empleado empleado) {

        // Validar nombre

        if (empleado.getNombre() == null ||
                empleado.getNombre().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El nombre no puede quedar vacío."
            );
        }

        // Validar departamento

        if (empleado.getDepartamento() == null ||
                empleado.getDepartamento().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El departamento no puede quedar vacío."
            );
        }

        // Validar salario

        if (empleado.getSalario() <= 0) {

            throw new IllegalArgumentException(
                    "El salario debe ser mayor a cero."
            );
        }

        // Validar fecha

        if (empleado.getFechaContratacion() == null) {

            throw new IllegalArgumentException(
                    "Debe ingresar la fecha de contratación."
            );
        }

        // La fecha no puede ser futura

        if (empleado.getFechaContratacion()
                .isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "La fecha de contratación no puede ser futura."
            );
        }

        // Validar tipo de contrato

        if (empleado.getTipoContrato() == null ||
                empleado.getTipoContrato().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Debe seleccionar un tipo de contrato."
            );
        }
    }
}