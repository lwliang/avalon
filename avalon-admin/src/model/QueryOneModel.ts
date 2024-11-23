/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

import ServiceModel from './ServiceModel'

export default interface QueryOneModel extends ServiceModel {
  fields: string
  rpnCondition: string

}
