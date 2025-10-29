/**
 * Constantes de la aplicación
 * Sistema de Gestión Veterinaria PetSalud
 */

// API Configuration
export const API_CONFIG = {
  BASE_URL: 'http://localhost:8080/api',
  TIMEOUT: 30000,
};

// API Endpoints
export const API_ENDPOINTS = {
  DUENOS: '/duenos',
  MASCOTAS: '/mascotas',
  VETERINARIOS: '/veterinarios',
  TECNICOS: '/tecnicos',
  ORDENES: '/ordenes',
  TOMA_MUESTRAS: '/toma-muestras',
  RESULTADOS: '/resultados',
  FACTURAS: '/facturas',
  USUARIOS: '/usuarios',
  REPORTES: '/reportes',
};

// Estados de Orden
export const ESTADOS_ORDEN = {
  PENDIENTE: 'PENDIENTE',
  EN_PROCESO: 'EN_PROCESO',
  COMPLETADA: 'COMPLETADA',
  CANCELADA: 'CANCELADA',
};

export const ESTADOS_ORDEN_LISTA = [
  { value: 'PENDIENTE', label: 'Pendiente' },
  { value: 'EN_PROCESO', label: 'En Proceso' },
  { value: 'COMPLETADA', label: 'Completada' },
  { value: 'CANCELADA', label: 'Cancelada' },
];

// Prioridades
export const PRIORIDADES = {
  URGENTE: 'URGENTE',
  NORMAL: 'NORMAL',
  BAJA: 'BAJA',
};

export const PRIORIDADES_LISTA = [
  { value: 'URGENTE', label: 'Urgente', color: 'danger' },
  { value: 'NORMAL', label: 'Normal', color: 'primary' },
  { value: 'BAJA', label: 'Baja', color: 'secondary' },
];

// Estados de Factura
export const ESTADOS_FACTURA = {
  PENDIENTE: 'PENDIENTE',
  PAGADA: 'PAGADA',
  ANULADA: 'ANULADA',
};

export const ESTADOS_FACTURA_LISTA = [
  { value: 'PENDIENTE', label: 'Pendiente', color: 'warning' },
  { value: 'PAGADA', label: 'Pagada', color: 'success' },
  { value: 'ANULADA', label: 'Anulada', color: 'danger' },
];

// Métodos de Pago
export const METODOS_PAGO = {
  EFECTIVO: 'EFECTIVO',
  TARJETA: 'TARJETA',
  TRANSFERENCIA: 'TRANSFERENCIA',
  YAPE: 'YAPE',
  PLIN: 'PLIN',
};

export const METODOS_PAGO_LISTA = [
  { value: 'EFECTIVO', label: 'Efectivo' },
  { value: 'TARJETA', label: 'Tarjeta' },
  { value: 'TRANSFERENCIA', label: 'Transferencia' },
  { value: 'YAPE', label: 'Yape' },
  { value: 'PLIN', label: 'Plin' },
];

// Estados de Toma de Muestra
export const ESTADOS_TOMA_MUESTRA = {
  PROGRAMADA: 'PROGRAMADA',
  REALIZADA: 'REALIZADA',
  PROCESANDO: 'PROCESANDO',
  COMPLETADA: 'COMPLETADA',
};

export const ESTADOS_TOMA_MUESTRA_LISTA = [
  { value: 'PROGRAMADA', label: 'Programada', color: 'info' },
  { value: 'REALIZADA', label: 'Realizada', color: 'primary' },
  { value: 'PROCESANDO', label: 'Procesando', color: 'warning' },
  { value: 'COMPLETADA', label: 'Completada', color: 'success' },
];

// Tipos de Examen
export const TIPOS_EXAMEN = [
  'Hemograma',
  'Bioquímica',
  'Urianálisis',
  'Coprológico',
  'Rayos X',
  'Ecografía',
  'Electrocardiograma',
  'Cultivo',
  'Citología',
  'Histopatología',
];

// Especies
export const ESPECIES = [
  'Perro',
  'Gato',
  'Ave',
  'Conejo',
  'Hámster',
  'Cobayo',
  'Reptil',
  'Otro',
];

// Sexos
export const SEXOS = [
  { value: 'Macho', label: 'Macho' },
  { value: 'Hembra', label: 'Hembra' },
];

// Tipos de Muestra
export const TIPOS_MUESTRA = [
  'Sangre',
  'Orina',
  'Heces',
  'Piel',
  'Pelo',
  'Saliva',
  'Tejido',
  'Líquido cefalorraquídeo',
  'Líquido sinovial',
];

// Métodos de Obtención de Muestra
export const METODOS_OBTENCION = [
  'Venopunción',
  'Cistocentesis',
  'Raspado cutáneo',
  'Biopsia',
  'Hisopado',
  'Punción',
  'Recolección natural',
];

// Roles de Usuario
export const ROLES = {
  ADMIN: 'ADMIN',
  VETERINARIO: 'VETERINARIO',
  TECNICO: 'TECNICO',
  RECEPCIONISTA: 'RECEPCIONISTA',
};

export const ROLES_LISTA = [
  { value: 'ADMIN', label: 'Administrador' },
  { value: 'VETERINARIO', label: 'Veterinario' },
  { value: 'TECNICO', label: 'Técnico' },
  { value: 'RECEPCIONISTA', label: 'Recepcionista' },
];

// Especialidades Veterinarias
export const ESPECIALIDADES_VETERINARIAS = [
  'Medicina General',
  'Cirugía',
  'Dermatología',
  'Oftalmología',
  'Cardiología',
  'Neurología',
  'Oncología',
  'Laboratorio',
  'Diagnóstico por Imagen',
  'Nutrición',
];

// Especialidades Técnicas
export const ESPECIALIDADES_TECNICAS = [
  'Hematología',
  'Microbiología',
  'Química Clínica',
  'Parasitología',
  'Citología',
  'Histopatología',
  'Imagenología',
];

// Tipos de Servicio para Facturación
export const TIPOS_SERVICIO = [
  { value: 'EXAMEN', label: 'Examen de Laboratorio' },
  { value: 'CONSULTA', label: 'Consulta Veterinaria' },
  { value: 'MEDICAMENTO', label: 'Medicamento' },
  { value: 'VACUNA', label: 'Vacuna' },
  { value: 'CIRUGIA', label: 'Cirugía' },
  { value: 'HOSPITALIZACION', label: 'Hospitalización' },
  { value: 'PELUQUERIA', label: 'Peluquería' },
  { value: 'OTRO', label: 'Otro' },
];

// Configuración de Paginación
export const PAGINATION = {
  DEFAULT_PAGE_SIZE: 10,
  PAGE_SIZE_OPTIONS: [5, 10, 25, 50, 100],
};

// Configuración de Notificaciones
export const NOTIFICATION_CONFIG = {
  DURATION: 3000, // milisegundos
  POSITION: 'top-right',
};

// Formatos de Fecha
export const DATE_FORMATS = {
  DISPLAY: 'dd/MM/yyyy',
  DISPLAY_TIME: 'dd/MM/yyyy HH:mm',
  API: 'yyyy-MM-dd',
  API_TIME: 'yyyy-MM-ddTHH:mm:ss',
};

// IGV (Impuesto General a las Ventas - Perú)
export const IGV_RATE = 0.18; // 18%

// Mensajes de Validación
export const VALIDATION_MESSAGES = {
  REQUIRED: 'Este campo es obligatorio',
  EMAIL: 'Debe ingresar un email válido',
  MIN_LENGTH: 'Debe tener al menos {min} caracteres',
  MAX_LENGTH: 'No puede exceder {max} caracteres',
  PATTERN: 'El formato no es válido',
  MIN: 'El valor mínimo es {min}',
  MAX: 'El valor máximo es {max}',
  DNI: 'DNI debe tener 8 dígitos',
  PHONE: 'Teléfono no válido',
  PASSWORD_MISMATCH: 'Las contraseñas no coinciden',
};

// Regex Patterns
export const PATTERNS = {
  DNI: /^\d{8}$/,
  PHONE: /^[0-9]{7,15}$/,
  EMAIL: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
  ALPHANUMERIC: /^[a-zA-Z0-9\s]+$/,
  ONLY_NUMBERS: /^\d+$/,
  DECIMAL: /^\d+(\.\d{1,2})?$/,
};

// Colores para Estados
export const STATUS_COLORS = {
  PENDIENTE: '#ffc107',
  EN_PROCESO: '#17a2b8',
  COMPLETADA: '#28a745',
  CANCELADA: '#dc3545',
  PAGADA: '#28a745',
  ANULADA: '#6c757d',
  URGENTE: '#dc3545',
  NORMAL: '#007bff',
  BAJA: '#6c757d',
};

// Configuración Local Storage Keys
export const STORAGE_KEYS = {
  USER: 'petsalud_user',
  TOKEN: 'petsalud_token',
  PREFERENCES: 'petsalud_preferences',
};

// Límites de Archivo
export const FILE_LIMITS = {
  MAX_SIZE: 10 * 1024 * 1024, // 10MB
  ALLOWED_TYPES: ['image/jpeg', 'image/png', 'application/pdf'],
};

// Información de la Empresa
export const EMPRESA_INFO = {
  NOMBRE: 'Veterinaria PetSalud',
  RUC: '20123456789',
  DIRECCION: 'Av. Principal 123, Lima, Perú',
  TELEFONO: '(01) 234-5678',
  EMAIL: 'info@petsalud.com',
  WEBSITE: 'https://petsalud.com',
  HORARIO: 'Lun-Vie: 8:00-20:00, Sáb: 9:00-14:00',
};