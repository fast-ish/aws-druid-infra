import React from 'react';

interface IconProps {
  className?: string;
  size?: number;
}

export const DruidIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#14B8A6"/>
    <path d="M40 18L56 28V52L40 62L24 52V28L40 18Z" stroke="white" strokeWidth="2" fill="none"/>
    <circle cx="40" cy="40" r="10" fill="white" fillOpacity="0.9"/>
    <path d="M40 30V50" stroke="#14B8A6" strokeWidth="2"/>
    <path d="M30 40H50" stroke="#14B8A6" strokeWidth="2"/>
    <circle cx="40" cy="22" r="3" fill="white"/>
    <circle cx="40" cy="58" r="3" fill="white"/>
    <circle cx="26" cy="31" r="3" fill="white"/>
    <circle cx="54" cy="31" r="3" fill="white"/>
    <circle cx="26" cy="49" r="3" fill="white"/>
    <circle cx="54" cy="49" r="3" fill="white"/>
  </svg>
);

export const CoordinatorIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#0D9488"/>
    <circle cx="40" cy="40" r="16" stroke="white" strokeWidth="2" fill="none"/>
    <circle cx="40" cy="40" r="6" fill="white"/>
    <path d="M40 24V32" stroke="white" strokeWidth="2"/>
    <path d="M40 48V56" stroke="white" strokeWidth="2"/>
    <path d="M24 40H32" stroke="white" strokeWidth="2"/>
    <path d="M48 40H56" stroke="white" strokeWidth="2"/>
    <circle cx="40" cy="20" r="4" fill="white" fillOpacity="0.6"/>
    <circle cx="40" cy="60" r="4" fill="white" fillOpacity="0.6"/>
    <circle cx="20" cy="40" r="4" fill="white" fillOpacity="0.6"/>
    <circle cx="60" cy="40" r="4" fill="white" fillOpacity="0.6"/>
  </svg>
);

export const BrokerIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#0F766E"/>
    <rect x="28" y="24" width="24" height="32" rx="4" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M18 32L28 40L18 48" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M62 32L52 40L62 48" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M34 32H46" stroke="white" strokeWidth="2"/>
    <path d="M34 40H46" stroke="white" strokeWidth="2"/>
    <path d="M34 48H46" stroke="white" strokeWidth="2"/>
  </svg>
);

export const HistoricalIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#115E59"/>
    <rect x="20" y="24" width="40" height="32" rx="4" stroke="white" strokeWidth="2" fill="none"/>
    <rect x="26" y="30" width="12" height="8" fill="white" fillOpacity="0.8"/>
    <rect x="42" y="30" width="12" height="8" fill="white" fillOpacity="0.6"/>
    <rect x="26" y="42" width="12" height="8" fill="white" fillOpacity="0.4"/>
    <rect x="42" y="42" width="12" height="8" fill="white" fillOpacity="0.2"/>
    <path d="M18 60H62" stroke="white" strokeWidth="2"/>
    <path d="M26 56V60" stroke="white" strokeWidth="2"/>
    <path d="M40 56V60" stroke="white" strokeWidth="2"/>
    <path d="M54 56V60" stroke="white" strokeWidth="2"/>
  </svg>
);

export const OverlordIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#134E4A"/>
    <path d="M40 20L56 30V50L40 60L24 50V30L40 20Z" stroke="white" strokeWidth="2" fill="none"/>
    <circle cx="40" cy="40" r="8" fill="white"/>
    <path d="M40 20V32" stroke="white" strokeWidth="2"/>
    <path d="M56 30L48 36" stroke="white" strokeWidth="2"/>
    <path d="M24 30L32 36" stroke="white" strokeWidth="2"/>
    <path d="M40 60V48" stroke="white" strokeWidth="2"/>
    <path d="M56 50L48 44" stroke="white" strokeWidth="2"/>
    <path d="M24 50L32 44" stroke="white" strokeWidth="2"/>
  </svg>
);

export const RouterIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#0D9488"/>
    <circle cx="40" cy="40" r="12" stroke="white" strokeWidth="2" fill="none"/>
    <circle cx="40" cy="40" r="4" fill="white"/>
    <path d="M16 40H28" stroke="white" strokeWidth="2"/>
    <path d="M52 40H64" stroke="white" strokeWidth="2"/>
    <path d="M40 16V28" stroke="white" strokeWidth="2"/>
    <path d="M40 52V64" stroke="white" strokeWidth="2"/>
    <path d="M24 24L31 31" stroke="white" strokeWidth="2"/>
    <path d="M49 49L56 56" stroke="white" strokeWidth="2"/>
    <path d="M24 56L31 49" stroke="white" strokeWidth="2"/>
    <path d="M49 31L56 24" stroke="white" strokeWidth="2"/>
  </svg>
);

export const MiddleManagerIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#14B8A6"/>
    <rect x="24" y="28" width="14" height="24" rx="2" stroke="white" strokeWidth="2" fill="none"/>
    <rect x="42" y="28" width="14" height="24" rx="2" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M38 36H42" stroke="white" strokeWidth="2"/>
    <path d="M38 44H42" stroke="white" strokeWidth="2"/>
    <path d="M27 34H35" stroke="white" strokeWidth="2"/>
    <path d="M27 40H35" stroke="white" strokeWidth="2"/>
    <path d="M27 46H35" stroke="white" strokeWidth="2"/>
    <path d="M45 34H53" stroke="white" strokeWidth="2"/>
    <path d="M45 40H53" stroke="white" strokeWidth="2"/>
    <path d="M45 46H53" stroke="white" strokeWidth="2"/>
    <path d="M40 18V28" stroke="white" strokeWidth="2"/>
    <path d="M40 52V62" stroke="white" strokeWidth="2"/>
  </svg>
);

export const VPCIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#8C4FFF"/>
    <path d="M40 16L58 26V54L40 64L22 54V26L40 16Z" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M40 16V64" stroke="white" strokeWidth="2"/>
    <path d="M22 26L58 54" stroke="white" strokeWidth="2"/>
    <path d="M58 26L22 54" stroke="white" strokeWidth="2"/>
  </svg>
);

export const EKSIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#FF9900"/>
    <circle cx="40" cy="40" r="18" stroke="white" strokeWidth="2" fill="none"/>
    <circle cx="40" cy="40" r="8" fill="white"/>
    <circle cx="40" cy="18" r="4" fill="white"/>
    <circle cx="40" cy="62" r="4" fill="white"/>
    <circle cx="18" cy="40" r="4" fill="white"/>
    <circle cx="62" cy="40" r="4" fill="white"/>
    <path d="M40 22V32" stroke="white" strokeWidth="2"/>
    <path d="M40 48V58" stroke="white" strokeWidth="2"/>
    <path d="M22 40H32" stroke="white" strokeWidth="2"/>
    <path d="M48 40H58" stroke="white" strokeWidth="2"/>
  </svg>
);

export const S3Icon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#569A31"/>
    <path d="M40 18L60 28V52L40 62L20 52V28L40 18Z" fill="white" fillOpacity="0.2" stroke="white" strokeWidth="2"/>
    <path d="M40 18V62" stroke="white" strokeWidth="2"/>
    <path d="M20 28L60 28" stroke="white" strokeWidth="2"/>
    <path d="M20 52L60 52" stroke="white" strokeWidth="2"/>
  </svg>
);

export const RDSIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#3B48CC"/>
    <ellipse cx="40" cy="28" rx="18" ry="8" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M22 28V52C22 56 30 60 40 60C50 60 58 56 58 52V28" stroke="white" strokeWidth="2"/>
    <ellipse cx="40" cy="40" rx="18" ry="8" stroke="white" strokeWidth="2" fill="none"/>
    <ellipse cx="40" cy="52" rx="18" ry="8" stroke="white" strokeWidth="2" fill="none"/>
  </svg>
);

export const MSKIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#FF9900"/>
    <rect x="20" y="32" width="40" height="16" rx="4" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M28 38H36" stroke="white" strokeWidth="2"/>
    <path d="M44 38H52" stroke="white" strokeWidth="2"/>
    <path d="M28 42H36" stroke="white" strokeWidth="2"/>
    <path d="M44 42H52" stroke="white" strokeWidth="2"/>
    <path d="M16 40H20" stroke="white" strokeWidth="2"/>
    <path d="M60 40H64" stroke="white" strokeWidth="2"/>
    <path d="M32 20L40 32L48 20" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M32 60L40 48L48 60" stroke="white" strokeWidth="2" fill="none"/>
  </svg>
);

export const IAMIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#DD344C"/>
    <circle cx="40" cy="32" r="10" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M24 58C24 48 32 42 40 42C48 42 56 48 56 58" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M40 22V42" stroke="white" strokeWidth="2"/>
    <circle cx="52" cy="28" r="4" fill="white"/>
    <path d="M50 28L52 30L54 26" stroke="#DD344C" strokeWidth="1.5"/>
  </svg>
);

export const HelmIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#0F1689"/>
    <circle cx="40" cy="40" r="18" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M40 22V58" stroke="white" strokeWidth="2"/>
    <path d="M22 40H58" stroke="white" strokeWidth="2"/>
    <circle cx="40" cy="40" r="6" fill="white"/>
    <path d="M30 30L50 50" stroke="white" strokeWidth="2"/>
    <path d="M50 30L30 50" stroke="white" strokeWidth="2"/>
  </svg>
);

export const CDKIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#232F3E"/>
    <path d="M24 24L40 16L56 24V40L40 48L24 40V24Z" fill="#FF9900" stroke="#FF9900" strokeWidth="2"/>
    <path d="M24 40L40 48L56 40V56L40 64L24 56V40Z" fill="#FF9900" fillOpacity="0.6" stroke="#FF9900" strokeWidth="2"/>
    <text x="40" y="38" textAnchor="middle" fill="white" fontSize="12" fontWeight="bold">CDK</text>
  </svg>
);

export const KubernetesIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#326CE5"/>
    <path d="M40 18L56 28V52L40 62L24 52V28L40 18Z" stroke="white" strokeWidth="2" fill="none"/>
    <circle cx="40" cy="40" r="8" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M40 18V32" stroke="white" strokeWidth="2"/>
    <path d="M40 48V62" stroke="white" strokeWidth="2"/>
    <path d="M24 28L32 36" stroke="white" strokeWidth="2"/>
    <path d="M56 28L48 36" stroke="white" strokeWidth="2"/>
    <path d="M24 52L32 44" stroke="white" strokeWidth="2"/>
    <path d="M56 52L48 44" stroke="white" strokeWidth="2"/>
  </svg>
);

export const KarpenterIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#0D47A1"/>
    <rect x="20" y="24" width="16" height="16" rx="2" stroke="white" strokeWidth="2" fill="none"/>
    <rect x="44" y="24" width="16" height="16" rx="2" stroke="white" strokeWidth="2" fill="none"/>
    <rect x="20" y="44" width="16" height="16" rx="2" stroke="white" strokeWidth="2" fill="none"/>
    <rect x="44" y="44" width="16" height="16" rx="2" stroke="white" strokeWidth="2" strokeDasharray="4 2" fill="none"/>
    <path d="M36 32H44" stroke="white" strokeWidth="2"/>
    <path d="M28 40V44" stroke="white" strokeWidth="2"/>
    <path d="M52 40V44" stroke="white" strokeWidth="2"/>
  </svg>
);

export const SecretsManagerIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#DD344C"/>
    <rect x="28" y="36" width="24" height="24" rx="4" stroke="white" strokeWidth="2" fill="none"/>
    <circle cx="40" cy="30" r="10" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M40 20V24" stroke="white" strokeWidth="2"/>
    <circle cx="40" cy="48" r="4" fill="white"/>
    <path d="M40 52V56" stroke="white" strokeWidth="2"/>
  </svg>
);

export const ALBIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#8C4FFF"/>
    <circle cx="40" cy="40" r="16" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M28 40H52" stroke="white" strokeWidth="2"/>
    <path d="M40 28V52" stroke="white" strokeWidth="2"/>
    <circle cx="40" cy="40" r="4" fill="white"/>
    <path d="M16 40H24" stroke="white" strokeWidth="2"/>
    <path d="M56 40H64" stroke="white" strokeWidth="2"/>
    <path d="M40 16V24" stroke="white" strokeWidth="2"/>
    <path d="M40 56V64" stroke="white" strokeWidth="2"/>
  </svg>
);

export const CloudWatchIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#FF4F8B"/>
    <circle cx="40" cy="40" r="18" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M40 28V40L50 46" stroke="white" strokeWidth="2" strokeLinecap="round"/>
    <circle cx="40" cy="40" r="3" fill="white"/>
  </svg>
);

export const GrafanaIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#F46800"/>
    <circle cx="40" cy="40" r="18" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M26 50L34 38L42 44L50 32L54 36" stroke="white" strokeWidth="2" fill="none" strokeLinecap="round"/>
    <circle cx="34" cy="38" r="3" fill="white"/>
    <circle cx="42" cy="44" r="3" fill="white"/>
    <circle cx="50" cy="32" r="3" fill="white"/>
  </svg>
);

export const CertManagerIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#326CE5"/>
    <rect x="24" y="20" width="32" height="40" rx="4" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M32 32H48" stroke="white" strokeWidth="2"/>
    <path d="M32 40H48" stroke="white" strokeWidth="2"/>
    <path d="M32 48H44" stroke="white" strokeWidth="2"/>
    <circle cx="52" cy="52" r="10" fill="#4CAF50" stroke="white" strokeWidth="2"/>
    <path d="M47 52L50 55L57 48" stroke="white" strokeWidth="2"/>
  </svg>
);

export const ZookeeperIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#6E4C13"/>
    <circle cx="40" cy="28" r="8" stroke="white" strokeWidth="2" fill="none"/>
    <circle cx="28" cy="52" r="8" stroke="white" strokeWidth="2" fill="none"/>
    <circle cx="52" cy="52" r="8" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M40 36V44" stroke="white" strokeWidth="2"/>
    <path d="M36 44L28 44" stroke="white" strokeWidth="2"/>
    <path d="M44 44L52 44" stroke="white" strokeWidth="2"/>
  </svg>
);

export const DockerIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#2496ED"/>
    <rect x="18" y="36" width="8" height="8" fill="white"/>
    <rect x="28" y="36" width="8" height="8" fill="white"/>
    <rect x="38" y="36" width="8" height="8" fill="white"/>
    <rect x="28" y="26" width="8" height="8" fill="white"/>
    <rect x="38" y="26" width="8" height="8" fill="white"/>
    <rect x="48" y="36" width="8" height="8" fill="white"/>
    <path d="M14 48C14 48 20 56 40 56C60 56 66 48 66 48" stroke="white" strokeWidth="2"/>
  </svg>
);

export const PostgreSQLIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#336791"/>
    <ellipse cx="40" cy="28" rx="16" ry="8" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M24 28V52C24 56 32 60 40 60C48 60 56 56 56 52V28" stroke="white" strokeWidth="2"/>
    <ellipse cx="40" cy="40" rx="16" ry="6" stroke="white" strokeWidth="2" fill="none"/>
    <ellipse cx="40" cy="52" rx="16" ry="6" stroke="white" strokeWidth="2" fill="none"/>
    <circle cx="40" cy="28" r="4" fill="white"/>
  </svg>
);

export const ExternalSecretsIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#7C3AED"/>
    <rect x="24" y="32" width="20" height="16" rx="2" stroke="white" strokeWidth="2" fill="none"/>
    <circle cx="34" cy="40" r="4" fill="white"/>
    <path d="M44 36L56 36" stroke="white" strokeWidth="2"/>
    <path d="M44 44L56 44" stroke="white" strokeWidth="2"/>
    <path d="M52 32L56 36L52 40" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M52 40L56 44L52 48" stroke="white" strokeWidth="2" fill="none"/>
  </svg>
);

export const TLSIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#16A34A"/>
    <rect x="28" y="36" width="24" height="20" rx="4" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M32 36V28C32 24 36 20 40 20C44 20 48 24 48 28V36" stroke="white" strokeWidth="2" fill="none"/>
    <circle cx="40" cy="46" r="4" fill="white"/>
    <path d="M40 50V52" stroke="white" strokeWidth="2"/>
  </svg>
);

export const MetricsServerIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#326CE5"/>
    <rect x="20" y="48" width="8" height="12" fill="white"/>
    <rect x="32" y="40" width="8" height="20" fill="white"/>
    <rect x="44" y="32" width="8" height="28" fill="white"/>
    <rect x="56" y="24" width="8" height="36" fill="white"/>
    <path d="M16 60H64" stroke="white" strokeWidth="2"/>
  </svg>
);

export const ExternalDNSIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#FF9900"/>
    <circle cx="40" cy="40" r="16" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M32 40H48" stroke="white" strokeWidth="2"/>
    <path d="M40 32V48" stroke="white" strokeWidth="2"/>
    <path d="M16 40H24" stroke="white" strokeWidth="2"/>
    <path d="M56 40H64" stroke="white" strokeWidth="2"/>
    <text x="40" y="56" textAnchor="middle" fill="white" fontSize="8" fontWeight="bold">DNS</text>
  </svg>
);

export const ReloaderIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#14B8A6"/>
    <circle cx="40" cy="40" r="16" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M48 32L40 40L48 48" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M32 32L40 40L32 48" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M40 22V28" stroke="white" strokeWidth="2"/>
    <path d="M40 52V58" stroke="white" strokeWidth="2"/>
  </svg>
);

export const GoldilocksIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#FFB800"/>
    <rect x="20" y="44" width="12" height="16" rx="2" stroke="white" strokeWidth="2" fill="none"/>
    <rect x="34" y="32" width="12" height="28" rx="2" stroke="white" strokeWidth="2" fill="none"/>
    <rect x="48" y="20" width="12" height="40" rx="2" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M26 38L40 26L54 14" stroke="white" strokeWidth="2" strokeDasharray="4 2"/>
    <circle cx="40" cy="26" r="3" fill="white"/>
  </svg>
);

export const VeleroIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#1A73E8"/>
    <path d="M40 18L56 30V50L40 62L24 50V30L40 18Z" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M32 36L40 44L48 36" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M40 28V44" stroke="white" strokeWidth="2"/>
    <path d="M28 52H52" stroke="white" strokeWidth="2"/>
    <circle cx="40" cy="52" r="3" fill="white"/>
  </svg>
);

export const KyvernoIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#EF6C00"/>
    <path d="M40 18L56 28V52L40 62L24 52V28L40 18Z" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M40 28V52" stroke="white" strokeWidth="2"/>
    <path d="M28 40H52" stroke="white" strokeWidth="2"/>
    <circle cx="40" cy="40" r="8" stroke="white" strokeWidth="2" fill="none"/>
    <path d="M36 40L39 43L46 36" stroke="white" strokeWidth="2"/>
  </svg>
);

export const NodeTerminationHandlerIcon: React.FC<IconProps> = ({ className = '', size = 48 }) => (
  <svg className={className} width={size} height={size} viewBox="0 0 80 80" fill="none" xmlns="http://www.w3.org/2000/svg">
    <rect width="80" height="80" rx="8" fill="#D32F2F"/>
    <rect x="24" y="24" width="32" height="24" rx="4" stroke="white" strokeWidth="2" fill="none"/>
    <circle cx="32" cy="36" r="4" fill="white"/>
    <circle cx="48" cy="36" r="4" fill="white"/>
    <path d="M40 48V58" stroke="white" strokeWidth="2"/>
    <path d="M32 58H48" stroke="white" strokeWidth="2"/>
    <path d="M36 54L40 58L44 54" stroke="white" strokeWidth="2" fill="none"/>
  </svg>
);
