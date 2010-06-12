# Spring Twitter

This module provides an interface to twitter4j to ease use in spring projects that are using constretto for configuration.

## Usage

You will need a spring context configuration similar to::

    <context:component-scan base-package="your_root_package_here"/>

    <constretto:configuration>
        <constretto:stores>
            <constretto:properties-store>
                <constretto:resource location="classpath:twitter.properties"/>
            </constretto:properties-store>
        </constretto:stores>
    </constretto:configuration>

    <bean id="twitter" class="net.chrissearle.spring.twitter.spring.TwitterFactoryBean"/>

Your constretto config may use any store or combination of stores - but the following properties must be defined:

    twitter.consumer.key=
    twitter.consumer.secret=
    twitter.token=
    twitter.token.secret=
    twitter.active=

Active can be TRUE or FALSE. FALSE should disable most calls to twitter - but - not the actual twitter object initialization. This is to allow temporarily disabling posting of updates etc when testing.

## References

* Twitter4j: http://twitter4j.org/
* Constretto: http://constretto.org/