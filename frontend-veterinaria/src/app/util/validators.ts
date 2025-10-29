/**
 * Validadores personalizados para formularios
 * Sistema de Gestión Veterinaria PetSalud
 */

import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/**
 * Validador para DNI peruano (8 dígitos)
 */
export function dniValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const dniPattern = /^\d{8}$/;
    const valid = dniPattern.test(control.value);
    
    return valid ? null : { dni: { value: control.value } };
  };
}

/**
 * Validador para RUC peruano (11 dígitos)
 */
export function rucValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const rucPattern = /^\d{11}$/;
    const valid = rucPattern.test(control.value);
    
    return valid ? null : { ruc: { value: control.value } };
  };
}

/**
 * Validador para teléfono (7-15 dígitos)
 */
export function phoneValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const phonePattern = /^[0-9]{7,15}$/;
    const valid = phonePattern.test(control.value);
    
    return valid ? null : { phone: { value: control.value } };
  };
}

/**
 * Validador para email
 */
export function emailValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const valid = emailPattern.test(control.value);
    
    return valid ? null : { email: { value: control.value } };
  };
}

/**
 * Validador para números decimales (máximo 2 decimales)
 */
export function decimalValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const decimalPattern = /^\d+(\.\d{1,2})?$/;
    const valid = decimalPattern.test(control.value.toString());
    
    return valid ? null : { decimal: { value: control.value } };
  };
}

/**
 * Validador para números positivos
 */
export function positiveNumberValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const value = parseFloat(control.value);
    const valid = !isNaN(value) && value > 0;
    
    return valid ? null : { positiveNumber: { value: control.value } };
  };
}

/**
 * Validador para números no negativos (incluye 0)
 */
export function nonNegativeNumberValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value && control.value !== 0) {
      return null;
    }
    
    const value = parseFloat(control.value);
    const valid = !isNaN(value) && value >= 0;
    
    return valid ? null : { nonNegativeNumber: { value: control.value } };
  };
}

/**
 * Validador para fechas futuras
 */
export function futureDateValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const inputDate = new Date(control.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    const valid = inputDate > today;
    
    return valid ? null : { futureDate: { value: control.value } };
  };
}

/**
 * Validador para fechas pasadas
 */
export function pastDateValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const inputDate = new Date(control.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    const valid = inputDate < today;
    
    return valid ? null : { pastDate: { value: control.value } };
  };
}

/**
 * Validador para rango de fechas
 */
export function dateRangeValidator(startDateField: string, endDateField: string): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const startDate = control.get(startDateField)?.value;
    const endDate = control.get(endDateField)?.value;
    
    if (!startDate || !endDate) {
      return null;
    }
    
    const start = new Date(startDate);
    const end = new Date(endDate);
    
    const valid = start <= end;
    
    return valid ? null : { dateRange: true };
  };
}

/**
 * Validador para edad mínima
 */
export function minAgeValidator(minAge: number): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const age = parseInt(control.value, 10);
    const valid = !isNaN(age) && age >= minAge;
    
    return valid ? null : { minAge: { minAge, actualAge: control.value } };
  };
}

/**
 * Validador para edad máxima
 */
export function maxAgeValidator(maxAge: number): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const age = parseInt(control.value, 10);
    const valid = !isNaN(age) && age <= maxAge;
    
    return valid ? null : { maxAge: { maxAge, actualAge: control.value } };
  };
}

/**
 * Validador para comparar contraseñas
 */
export function passwordMatchValidator(passwordField: string, confirmPasswordField: string): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const password = control.get(passwordField)?.value;
    const confirmPassword = control.get(confirmPasswordField)?.value;
    
    if (!password || !confirmPassword) {
      return null;
    }
    
    const valid = password === confirmPassword;
    
    return valid ? null : { passwordMatch: true };
  };
}

/**
 * Validador para fortaleza de contraseña
 * Debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número
 */
export function strongPasswordValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const password = control.value;
    const hasMinLength = password.length >= 8;
    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasNumber = /[0-9]/.test(password);
    
    const valid = hasMinLength && hasUpperCase && hasLowerCase && hasNumber;
    
    if (!valid) {
      return {
        strongPassword: {
          hasMinLength,
          hasUpperCase,
          hasLowerCase,
          hasNumber
        }
      };
    }
    
    return null;
  };
}

/**
 * Validador para solo letras y espacios
 */
export function onlyLettersValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const pattern = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;
    const valid = pattern.test(control.value);
    
    return valid ? null : { onlyLetters: { value: control.value } };
  };
}

/**
 * Validador para solo números
 */
export function onlyNumbersValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const pattern = /^\d+$/;
    const valid = pattern.test(control.value);
    
    return valid ? null : { onlyNumbers: { value: control.value } };
  };
}

/**
 * Validador para alfanumérico
 */
export function alphanumericValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const pattern = /^[a-zA-Z0-9\s]+$/;
    const valid = pattern.test(control.value);
    
    return valid ? null : { alphanumeric: { value: control.value } };
  };
}

/**
 * Validador para URL
 */
export function urlValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    try {
      new URL(control.value);
      return null;
    } catch {
      return { url: { value: control.value } };
    }
  };
}

/**
 * Validador para código de muestra (formato personalizado)
 * Ejemplo: VM-2025-0001
 */
export function sampleCodeValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const pattern = /^[A-Z]{2}-\d{4}-\d{4}$/;
    const valid = pattern.test(control.value);
    
    return valid ? null : { sampleCode: { value: control.value } };
  };
}

/**
 * Validador para número de factura
 * Ejemplo: F001-00000001
 */
export function invoiceNumberValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const pattern = /^[FB]\d{3}-\d{8}$/;
    const valid = pattern.test(control.value);
    
    return valid ? null : { invoiceNumber: { value: control.value } };
  };
}

/**
 * Validador para colegiatura profesional
 */
export function collegeNumberValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    if (!control.value) {
      return null;
    }
    
    const pattern = /^[A-Z]{3}\d{5,8}$/;
    const valid = pattern.test(control.value);
    
    return valid ? null : { collegeNumber: { value: control.value } };
  };
}

/**
 * Utilidad para obtener mensajes de error personalizados
 */
export function getValidationErrorMessage(
  validatorName: string,
  validatorValue?: any
): string {
  const config: { [key: string]: string } = {
    required: 'Este campo es obligatorio',
    email: 'Debe ingresar un email válido',
    dni: 'DNI debe tener 8 dígitos',
    ruc: 'RUC debe tener 11 dígitos',
    phone: 'Teléfono no válido (7-15 dígitos)',
    decimal: 'Solo se permiten hasta 2 decimales',
    positiveNumber: 'Debe ser un número mayor a 0',
    nonNegativeNumber: 'Debe ser un número mayor o igual a 0',
    futureDate: 'La fecha debe ser futura',
    pastDate: 'La fecha debe ser pasada',
    dateRange: 'La fecha de inicio debe ser menor o igual a la fecha fin',
    passwordMatch: 'Las contraseñas no coinciden',
    strongPassword: 'La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número',
    onlyLetters: 'Solo se permiten letras',
    onlyNumbers: 'Solo se permiten números',
    alphanumeric: 'Solo se permiten letras y números',
    url: 'Debe ingresar una URL válida',
    sampleCode: 'Formato inválido (Ej: VM-2025-0001)',
    invoiceNumber: 'Formato inválido (Ej: F001-00000001)',
    collegeNumber: 'Formato inválido (Ej: CVP12345)',
    minlength: `Mínimo ${validatorValue?.requiredLength} caracteres`,
    maxlength: `Máximo ${validatorValue?.requiredLength} caracteres`,
    min: `Valor mínimo: ${validatorValue?.min}`,
    max: `Valor máximo: ${validatorValue?.max}`,
    minAge: `Edad mínima: ${validatorValue?.minAge} años`,
    maxAge: `Edad máxima: ${validatorValue?.maxAge} años`,
  };
  
  return config[validatorName] || 'Campo inválido';
}

/**
 * Función auxiliar para verificar si un control tiene un error específico
 */
export function hasError(control: AbstractControl | null, errorName: string): boolean {
  return control ? control.hasError(errorName) && (control.dirty || control.touched) : false;
}

/**
 * Función auxiliar para obtener el primer mensaje de error de un control
 */
export function getFirstError(control: AbstractControl | null): string | null {
  if (!control || !control.errors) {
    return null;
  }
  
  const firstErrorKey = Object.keys(control.errors)[0];
  return getValidationErrorMessage(firstErrorKey, control.errors[firstErrorKey]);
}