//
//  BMFModel.h
//  flutter_baidu_mapapi_base
//
//  Created by zbj on 2020/2/10.
//  Copyright © 2020 zbj. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BMFDicModel.h"
NS_ASSUME_NONNULL_BEGIN

/// 语言类型枚举
typedef enum {
    BMFLanguageTypeChinese = 0,   // 中文
    BMFLanguageTypeEnglish       // 英文
} BMFLanguageType;

@interface BMFModel : NSObject<BMFDicModel>

@end

NS_ASSUME_NONNULL_END
