package com.enableets.edu.pakage.core.factory;

import com.enableets.edu.pakage.core.bean.AbstractEnablePackageDecorator;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.core.source.PackageSource;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * package抽象工厂类
 * 1.提供package容器
 * 2.提供缓存策略
 * 3.定义模板方法来获取package
 */
public abstract class AbstractEnablePackageFactory<T extends AbstractEnablePackageDecorator> implements IEnablePackageFactory{

    private Map<String,AbstractEnablePackageDecorator> packageRegistry = new ConcurrentHashMap<>();

    private Configuration configuration;

    protected abstract AbstractEnablePackageDecorator fetchPackage(PackageSource source);

    public AbstractEnablePackageFactory(Configuration configuration){
        this.configuration = configuration;
    }


    @Override
    public T getPackage(PackageSource source) {
        AbstractEnablePackageDecorator pkg = packageRegistry.get(source.getId());
        if(Objects.isNull(pkg)) {
            pkg = fetchPackage(source);
            packageRegistry.put(source.getId(), pkg);
        }
        return (T)pkg;
    }

    protected Configuration getConfiguration(){
        return this.configuration;
    }
}
