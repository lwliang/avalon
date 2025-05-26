import type { IconType } from "../icon/types";

export interface RateProps {
  max?: number;
  size?: "large" | "default" | "small";
  disabled?: boolean;
  lowThreshold?: number;
  highThreshold?: number;
  colors?: [string, string, string];
  voidColor?: string;
  disabledVoidColor?: string;
  icon?: string | [string, string];
  iconType?: IconType;
  voidIcon?: string | [string, string];
  voidIconType?: IconType;
  required?: boolean;
}
