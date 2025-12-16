'use client';

import { motion } from 'framer-motion';
import React from 'react';
import { Popover, PopoverContent, PopoverTrigger } from './popover';
import { Badge } from './badge';
import { cn } from '@/lib/utils';
import { ExternalLink, Info, Zap, Database, Clock, Server, Shield } from 'lucide-react';

interface ServiceCardProps {
  icon: React.ReactNode;
  title: string;
  description: string;
  details?: string[];
  color?: string;
  delay?: number;
  className?: string;
  size?: 'sm' | 'md' | 'lg';
  docsUrl?: string;
  status?: 'active' | 'provisioning' | 'degraded';
  metrics?: {
    label: string;
    value: string;
  }[];
  tags?: string[];
}

export const ServiceCard: React.FC<ServiceCardProps> = ({
  icon,
  title,
  description,
  details = [],
  color = 'bg-slate-800',
  delay = 0,
  className = '',
  size = 'md',
  docsUrl,
  status = 'active',
  metrics = [],
  tags = [],
}) => {
  const sizeClasses = {
    sm: 'p-3 min-w-[140px]',
    md: 'p-4 min-w-[180px]',
    lg: 'p-5 min-w-[220px]',
  };

  const statusColors = {
    active: 'bg-green-500',
    provisioning: 'bg-yellow-500',
    degraded: 'bg-red-500',
  };

  const statusLabels = {
    active: 'Active',
    provisioning: 'Provisioning',
    degraded: 'Degraded',
  };

  return (
    <Popover>
      <PopoverTrigger asChild>
        <motion.div
          initial={{ opacity: 0, y: 20, scale: 0.95 }}
          animate={{ opacity: 1, y: 0, scale: 1 }}
          transition={{ duration: 0.5, delay, ease: 'easeOut' }}
          whileHover={{ scale: 1.02, y: -2 }}
          className={cn(
            sizeClasses[size],
            color,
            'rounded-xl border border-slate-700/50',
            'backdrop-blur-sm shadow-xl hover:shadow-2xl hover:border-teal-500/50',
            'transition-all duration-300 group cursor-pointer',
            'ring-offset-slate-950 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-teal-500 focus-visible:ring-offset-2',
            className
          )}
          tabIndex={0}
          role="button"
          aria-label={`View details for ${title}`}
        >
          <div className="flex items-start gap-3">
            <motion.div
              whileHover={{ rotate: [0, -5, 5, 0] }}
              transition={{ duration: 0.3 }}
              className="flex-shrink-0 relative"
            >
              {icon}
              <span className={cn(
                'absolute -top-1 -right-1 w-2 h-2 rounded-full',
                statusColors[status],
                status === 'provisioning' && 'animate-pulse'
              )} />
            </motion.div>
            <div className="flex-1 min-w-0">
              <h3 className="text-white font-semibold text-sm leading-tight mb-1 group-hover:text-teal-300 transition-colors flex items-center gap-1">
                {title}
                <Info className="w-3 h-3 text-slate-500 group-hover:text-teal-400 transition-colors" />
              </h3>
              <p className="text-slate-400 text-xs leading-relaxed">{description}</p>
              {details.length > 0 && (
                <motion.ul
                  initial={{ opacity: 0, height: 0 }}
                  animate={{ opacity: 1, height: 'auto' }}
                  className="mt-2 space-y-1"
                >
                  {details.slice(0, 2).map((detail, idx) => (
                    <li key={idx} className="text-slate-500 text-[10px] flex items-center gap-1">
                      <span className="w-1 h-1 rounded-full bg-teal-400"></span>
                      {detail}
                    </li>
                  ))}
                  {details.length > 2 && (
                    <li className="text-teal-400 text-[10px] flex items-center gap-1">
                      <span className="w-1 h-1 rounded-full bg-teal-400"></span>
                      +{details.length - 2} more...
                    </li>
                  )}
                </motion.ul>
              )}
            </div>
          </div>
        </motion.div>
      </PopoverTrigger>
      <PopoverContent
        className="w-80 bg-slate-900/95 backdrop-blur-xl border-slate-700/50 p-0 overflow-hidden"
        sideOffset={8}
      >
        {/* Popover Header */}
        <div className="bg-gradient-to-r from-slate-800 to-slate-900 p-4 border-b border-slate-700/50">
          <div className="flex items-start gap-3">
            <div className="flex-shrink-0">{icon}</div>
            <div className="flex-1">
              <div className="flex items-center gap-2 mb-1">
                <h4 className="font-bold text-white">{title}</h4>
                <Badge
                  variant="outline"
                  className={cn(
                    'text-[10px] border-0',
                    status === 'active' && 'bg-green-500/20 text-green-400',
                    status === 'provisioning' && 'bg-yellow-500/20 text-yellow-400',
                    status === 'degraded' && 'bg-red-500/20 text-red-400'
                  )}
                >
                  {statusLabels[status]}
                </Badge>
              </div>
              <p className="text-slate-400 text-sm">{description}</p>
            </div>
          </div>
        </div>

        {/* Popover Body */}
        <div className="p-4 space-y-4">
          {/* Details */}
          {details.length > 0 && (
            <div>
              <h5 className="text-xs font-semibold text-slate-300 mb-2 flex items-center gap-1">
                <Zap className="w-3 h-3 text-teal-400" />
                Features
              </h5>
              <ul className="space-y-1.5">
                {details.map((detail, idx) => (
                  <li key={idx} className="text-slate-400 text-xs flex items-center gap-2">
                    <span className="w-1.5 h-1.5 rounded-full bg-teal-400/60"></span>
                    {detail}
                  </li>
                ))}
              </ul>
            </div>
          )}

          {/* Metrics */}
          {metrics.length > 0 && (
            <div>
              <h5 className="text-xs font-semibold text-slate-300 mb-2 flex items-center gap-1">
                <Database className="w-3 h-3 text-purple-400" />
                Metrics
              </h5>
              <div className="grid grid-cols-2 gap-2">
                {metrics.map((metric, idx) => (
                  <div key={idx} className="bg-slate-800/50 rounded-lg p-2">
                    <div className="text-[10px] text-slate-500">{metric.label}</div>
                    <div className="text-sm font-mono text-white">{metric.value}</div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Tags */}
          {tags.length > 0 && (
            <div className="flex flex-wrap gap-1">
              {tags.map((tag, idx) => (
                <Badge
                  key={idx}
                  variant="secondary"
                  className="text-[10px] bg-slate-800 text-slate-300 border-slate-700"
                >
                  {tag}
                </Badge>
              ))}
            </div>
          )}

          {/* Docs Link */}
          {docsUrl && (
            <a
              href={docsUrl}
              target="_blank"
              rel="noopener noreferrer"
              className="flex items-center gap-2 text-xs text-teal-400 hover:text-teal-300 transition-colors cursor-pointer"
            >
              <ExternalLink className="w-3 h-3" />
              View Documentation
            </a>
          )}
        </div>

        {/* Quick Actions Footer */}
        <div className="bg-slate-800/50 border-t border-slate-700/50 px-4 py-2">
          <p className="text-[10px] text-slate-500 flex items-center gap-1">
            <Clock className="w-3 h-3" />
            Click to view in AWS Console
          </p>
        </div>
      </PopoverContent>
    </Popover>
  );
};

interface LayerProps {
  title: string;
  subtitle?: string;
  children: React.ReactNode;
  color?: string;
  delay?: number;
  className?: string;
}

export const Layer: React.FC<LayerProps> = ({
  title,
  subtitle,
  children,
  color = 'from-slate-800/50 to-slate-900/50',
  delay = 0,
  className = '',
}) => {
  return (
    <motion.div
      initial={{ opacity: 0, x: -20 }}
      animate={{ opacity: 1, x: 0 }}
      transition={{ duration: 0.6, delay }}
      className={cn(
        'relative bg-gradient-to-br rounded-2xl border border-slate-700/30 p-6',
        color,
        className
      )}
    >
      <div className="flex items-center gap-3 mb-4 cursor-pointer group/layer hover:opacity-90 transition-opacity">
        <div className="h-8 w-1 rounded-full bg-gradient-to-b from-teal-500 to-cyan-500 group-hover/layer:from-teal-400 group-hover/layer:to-cyan-400 transition-all"></div>
        <div>
          <h2 className="text-lg font-bold text-white group-hover/layer:text-teal-300 transition-colors">{title}</h2>
          {subtitle && <p className="text-slate-400 text-xs">{subtitle}</p>}
        </div>
      </div>
      <div className="relative">{children}</div>
    </motion.div>
  );
};

interface ConnectionLineProps {
  direction?: 'horizontal' | 'vertical' | 'diagonal';
  animated?: boolean;
  className?: string;
  label?: string;
}

export const ConnectionLine: React.FC<ConnectionLineProps> = ({
  direction = 'horizontal',
  animated = true,
  className = '',
  label,
}) => {
  const directionClasses = {
    horizontal: 'w-8 h-0.5',
    vertical: 'w-0.5 h-8',
    diagonal: 'w-8 h-8',
  };

  return (
    <div className={`relative flex items-center justify-center ${className}`}>
      {direction === 'diagonal' ? (
        <svg width="32" height="32" viewBox="0 0 32 32" className="text-teal-500/50">
          <motion.path
            d="M4 4 L28 28"
            stroke="currentColor"
            strokeWidth="2"
            fill="none"
            strokeDasharray={animated ? '4 4' : '0'}
            initial={{ pathLength: 0 }}
            animate={{ pathLength: 1 }}
            transition={{ duration: 1, repeat: animated ? Infinity : 0, ease: 'linear' }}
          />
        </svg>
      ) : (
        <motion.div
          className={`${directionClasses[direction]} bg-gradient-to-r from-teal-500/30 via-teal-400/50 to-teal-500/30 rounded-full`}
          initial={{ opacity: 0, scale: 0 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 0.3 }}
        >
          {animated && (
            <motion.div
              className={`${direction === 'horizontal' ? 'w-2 h-full' : 'w-full h-2'} bg-teal-400 rounded-full`}
              animate={{
                x: direction === 'horizontal' ? [0, 24, 0] : 0,
                y: direction === 'vertical' ? [0, 24, 0] : 0,
              }}
              transition={{ duration: 2, repeat: Infinity, ease: 'easeInOut' }}
            />
          )}
        </motion.div>
      )}
      {label && (
        <span className="absolute -top-4 text-[9px] text-slate-500 whitespace-nowrap">{label}</span>
      )}
    </div>
  );
};

interface DataFlowArrowProps {
  className?: string;
  direction?: 'right' | 'down' | 'left' | 'up';
  label?: string;
}

export const DataFlowArrow: React.FC<DataFlowArrowProps> = ({
  className = '',
  direction = 'right',
  label,
}) => {
  const rotations = {
    right: 'rotate-0',
    down: 'rotate-90',
    left: 'rotate-180',
    up: '-rotate-90',
  };

  return (
    <div className={`flex flex-col items-center gap-1 cursor-pointer hover:scale-110 transition-transform ${className}`}>
      {label && <span className="text-[9px] text-slate-500 whitespace-nowrap">{label}</span>}
      <motion.svg
        width="48"
        height="16"
        viewBox="0 0 48 16"
        className={`text-teal-400/60 ${rotations[direction]}`}
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ duration: 0.5 }}
      >
        <motion.path
          d="M0 8 L40 8"
          stroke="currentColor"
          strokeWidth="2"
          strokeDasharray="4 2"
          initial={{ pathLength: 0 }}
          animate={{ pathLength: 1 }}
          transition={{ duration: 1, delay: 0.5 }}
        />
        <motion.path
          d="M36 4 L44 8 L36 12"
          stroke="currentColor"
          strokeWidth="2"
          fill="none"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ duration: 0.3, delay: 1.5 }}
        />
      </motion.svg>
    </div>
  );
};

interface DruidComponentProps {
  icon: React.ReactNode;
  name: string;
  role: string;
  replicas: number;
  heap: string;
  instanceType: string;
  details: string[];
  delay?: number;
}

export const DruidComponent: React.FC<DruidComponentProps> = ({
  icon,
  name,
  role,
  replicas,
  heap,
  instanceType,
  details,
  delay = 0,
}) => {
  return (
    <ServiceCard
      icon={icon}
      title={name}
      description={role}
      details={details}
      delay={delay}
      status="active"
      metrics={[
        { label: 'Replicas', value: String(replicas) },
        { label: 'Heap', value: heap },
        { label: 'Instance', value: instanceType },
      ]}
      tags={['druid', name.toLowerCase(), 'statefulset']}
      docsUrl="https://druid.apache.org/docs/latest/design/"
    />
  );
};
