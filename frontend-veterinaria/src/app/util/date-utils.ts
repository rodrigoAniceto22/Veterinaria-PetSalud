/**
 * Utilidades para manejo de fechas
 * Sistema de Gestión Veterinaria PetSalud
 */

/**
 * Formatea una fecha al formato dd/MM/yyyy
 * @param date Fecha a formatear
 * @returns String con formato dd/MM/yyyy
 */
export function formatDate(date: Date | string | null | undefined): string {
  if (!date) return '';
  
  const d = typeof date === 'string' ? new Date(date) : date;
  
  if (isNaN(d.getTime())) return '';
  
  const day = String(d.getDate()).padStart(2, '0');
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const year = d.getFullYear();
  
  return `${day}/${month}/${year}`;
}

/**
 * Formatea una fecha y hora al formato dd/MM/yyyy HH:mm
 * @param date Fecha/hora a formatear
 * @returns String con formato dd/MM/yyyy HH:mm
 */
export function formatDateTime(date: Date | string | null | undefined): string {
  if (!date) return '';
  
  const d = typeof date === 'string' ? new Date(date) : date;
  
  if (isNaN(d.getTime())) return '';
  
  const day = String(d.getDate()).padStart(2, '0');
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const year = d.getFullYear();
  const hours = String(d.getHours()).padStart(2, '0');
  const minutes = String(d.getMinutes()).padStart(2, '0');
  
  return `${day}/${month}/${year} ${hours}:${minutes}`;
}

/**
 * Formatea una fecha al formato yyyy-MM-dd para APIs
 * @param date Fecha a formatear
 * @returns String con formato yyyy-MM-dd
 */
export function formatDateForAPI(date: Date | string | null | undefined): string {
  if (!date) return '';
  
  const d = typeof date === 'string' ? new Date(date) : date;
  
  if (isNaN(d.getTime())) return '';
  
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  
  return `${year}-${month}-${day}`;
}

/**
 * Formatea una fecha/hora al formato yyyy-MM-ddTHH:mm:ss para APIs
 * @param date Fecha/hora a formatear
 * @returns String con formato yyyy-MM-ddTHH:mm:ss
 */
export function formatDateTimeForAPI(date: Date | string | null | undefined): string {
  if (!date) return '';
  
  const d = typeof date === 'string' ? new Date(date) : date;
  
  if (isNaN(d.getTime())) return '';
  
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  const hours = String(d.getHours()).padStart(2, '0');
  const minutes = String(d.getMinutes()).padStart(2, '0');
  const seconds = String(d.getSeconds()).padStart(2, '0');
  
  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}

/**
 * Convierte una fecha del formato API (yyyy-MM-dd) a Date
 * @param dateString String con formato yyyy-MM-dd
 * @returns Objeto Date o null si inválido
 */
export function parseAPIDate(dateString: string | null | undefined): Date | null {
  if (!dateString) return null;
  
  const parts = dateString.split('-');
  if (parts.length !== 3) return null;
  
  const year = parseInt(parts[0], 10);
  const month = parseInt(parts[1], 10) - 1;
  const day = parseInt(parts[2], 10);
  
  const date = new Date(year, month, day);
  
  return isNaN(date.getTime()) ? null : date;
}

/**
 * Obtiene la fecha actual en formato yyyy-MM-dd
 * @returns String con la fecha actual
 */
export function getCurrentDate(): string {
  return formatDateForAPI(new Date());
}

/**
 * Obtiene la fecha/hora actual en formato yyyy-MM-ddTHH:mm:ss
 * @returns String con la fecha/hora actual
 */
export function getCurrentDateTime(): string {
  return formatDateTimeForAPI(new Date());
}

/**
 * Calcula la diferencia en días entre dos fechas
 * @param date1 Primera fecha
 * @param date2 Segunda fecha
 * @returns Número de días de diferencia
 */
export function getDaysDifference(
  date1: Date | string,
  date2: Date | string
): number {
  const d1 = typeof date1 === 'string' ? new Date(date1) : date1;
  const d2 = typeof date2 === 'string' ? new Date(date2) : date2;
  
  const diffTime = Math.abs(d2.getTime() - d1.getTime());
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  
  return diffDays;
}

/**
 * Calcula la diferencia en horas entre dos fechas
 * @param date1 Primera fecha
 * @param date2 Segunda fecha
 * @returns Número de horas de diferencia
 */
export function getHoursDifference(
  date1: Date | string,
  date2: Date | string
): number {
  const d1 = typeof date1 === 'string' ? new Date(date1) : date1;
  const d2 = typeof date2 === 'string' ? new Date(date2) : date2;
  
  const diffTime = Math.abs(d2.getTime() - d1.getTime());
  const diffHours = diffTime / (1000 * 60 * 60);
  
  return Math.round(diffHours * 100) / 100; // 2 decimales
}

/**
 * Verifica si una fecha es hoy
 * @param date Fecha a verificar
 * @returns true si la fecha es hoy
 */
export function isToday(date: Date | string): boolean {
  const d = typeof date === 'string' ? new Date(date) : date;
  const today = new Date();
  
  return (
    d.getDate() === today.getDate() &&
    d.getMonth() === today.getMonth() &&
    d.getFullYear() === today.getFullYear()
  );
}

/**
 * Verifica si una fecha es del futuro
 * @param date Fecha a verificar
 * @returns true si la fecha es del futuro
 */
export function isFutureDate(date: Date | string): boolean {
  const d = typeof date === 'string' ? new Date(date) : date;
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  
  return d > today;
}

/**
 * Verifica si una fecha es del pasado
 * @param date Fecha a verificar
 * @returns true si la fecha es del pasado
 */
export function isPastDate(date: Date | string): boolean {
  const d = typeof date === 'string' ? new Date(date) : date;
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  
  return d < today;
}

/**
 * Agrega días a una fecha
 * @param date Fecha base
 * @param days Número de días a agregar
 * @returns Nueva fecha
 */
export function addDays(date: Date | string, days: number): Date {
  const d = typeof date === 'string' ? new Date(date) : new Date(date);
  d.setDate(d.getDate() + days);
  return d;
}

/**
 * Agrega meses a una fecha
 * @param date Fecha base
 * @param months Número de meses a agregar
 * @returns Nueva fecha
 */
export function addMonths(date: Date | string, months: number): Date {
  const d = typeof date === 'string' ? new Date(date) : new Date(date);
  d.setMonth(d.getMonth() + months);
  return d;
}

/**
 * Obtiene el primer día del mes
 * @param date Fecha de referencia
 * @returns Primer día del mes
 */
export function getFirstDayOfMonth(date: Date | string = new Date()): Date {
  const d = typeof date === 'string' ? new Date(date) : new Date(date);
  return new Date(d.getFullYear(), d.getMonth(), 1);
}

/**
 * Obtiene el último día del mes
 * @param date Fecha de referencia
 * @returns Último día del mes
 */
export function getLastDayOfMonth(date: Date | string = new Date()): Date {
  const d = typeof date === 'string' ? new Date(date) : new Date(date);
  return new Date(d.getFullYear(), d.getMonth() + 1, 0);
}

/**
 * Obtiene el nombre del mes en español
 * @param monthNumber Número del mes (0-11)
 * @returns Nombre del mes
 */
export function getMonthName(monthNumber: number): string {
  const months = [
    'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
    'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
  ];
  return months[monthNumber] || '';
}

/**
 * Obtiene el nombre del día en español
 * @param dayNumber Número del día (0-6, donde 0 es Domingo)
 * @returns Nombre del día
 */
export function getDayName(dayNumber: number): string {
  const days = [
    'Domingo', 'Lunes', 'Martes', 'Miércoles', 
    'Jueves', 'Viernes', 'Sábado'
  ];
  return days[dayNumber] || '';
}

/**
 * Formatea una fecha de forma relativa (hace X días, hace X horas, etc.)
 * @param date Fecha a formatear
 * @returns String con formato relativo
 */
export function formatRelativeTime(date: Date | string): string {
  const d = typeof date === 'string' ? new Date(date) : date;
  const now = new Date();
  const diffSeconds = Math.floor((now.getTime() - d.getTime()) / 1000);
  
  if (diffSeconds < 60) {
    return 'Hace menos de un minuto';
  }
  
  const diffMinutes = Math.floor(diffSeconds / 60);
  if (diffMinutes < 60) {
    return `Hace ${diffMinutes} minuto${diffMinutes > 1 ? 's' : ''}`;
  }
  
  const diffHours = Math.floor(diffMinutes / 60);
  if (diffHours < 24) {
    return `Hace ${diffHours} hora${diffHours > 1 ? 's' : ''}`;
  }
  
  const diffDays = Math.floor(diffHours / 24);
  if (diffDays < 30) {
    return `Hace ${diffDays} día${diffDays > 1 ? 's' : ''}`;
  }
  
  const diffMonths = Math.floor(diffDays / 30);
  if (diffMonths < 12) {
    return `Hace ${diffMonths} mes${diffMonths > 1 ? 'es' : ''}`;
  }
  
  const diffYears = Math.floor(diffMonths / 12);
  return `Hace ${diffYears} año${diffYears > 1 ? 's' : ''}`;
}

/**
 * Valida si una fecha es válida
 * @param date Fecha a validar
 * @returns true si la fecha es válida
 */
export function isValidDate(date: any): boolean {
  if (!date) return false;
  
  const d = date instanceof Date ? date : new Date(date);
  return !isNaN(d.getTime());
}

/**
 * Obtiene la edad en años a partir de una fecha de nacimiento
 * @param birthDate Fecha de nacimiento
 * @returns Edad en años
 */
export function getAge(birthDate: Date | string): number {
  const birth = typeof birthDate === 'string' ? new Date(birthDate) : birthDate;
  const today = new Date();
  let age = today.getFullYear() - birth.getFullYear();
  const monthDiff = today.getMonth() - birth.getMonth();
  
  if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
    age--;
  }
  
  return age;
}