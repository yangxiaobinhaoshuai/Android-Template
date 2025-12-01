package me.yangxiaobin.android.kotlin.codelab.base.ability.nav


class DefaultNavEngine(
    private val activityDriver: ActivityNavDriver,
    private val fragmentDriver: FragmentNavDriver,
    private val dialogDriver: DialogNavDriver
) : NavEngine {

    // suspend 版：有返回值时用
    override suspend fun <R> execute(command: NavCommand<R>): R {
        return when (command) {

            is NavCommand.StartActivity<*, *> -> {
                @Suppress("UNCHECKED_CAST")
                val route = command.route as ActivityRoute<NavArgs, R>

                @Suppress("UNCHECKED_CAST")
                val args = command.args

                activityDriver.startActivity(route, args)
            }

            is NavCommand.ShowFragment<*, *> -> {
                @Suppress("UNCHECKED_CAST")
                val route = command.route as FragmentRoute<NavArgs, R>

                @Suppress("UNCHECKED_CAST")
                val args = command.args

                fragmentDriver.showFragment(
                    route = route,
                    args = args,
                    containerId = command.containerId,
                    addToBackStack = command.addToBackStack,
                    replace = command.replace,
                )
            }

            is NavCommand.ShowDialog<*, *> -> {
                @Suppress("UNCHECKED_CAST")
                val route = command.route as DialogRoute<NavArgs, R>

                @Suppress("UNCHECKED_CAST")
                val args = command.args
                dialogDriver.showDialog(route, args, command.tag)
            }

            is NavCommand.PopBackStack -> {
                fragmentDriver.popBackStack()
                @Suppress("UNCHECKED_CAST")
                Unit as R
            }
        }
    }

    // 同步版：只给 R = Unit 的 route 用
    @Suppress("UNCHECKED_CAST")
    override fun <R> executeNow(command: NavCommand<R>) {
        when (command) {

            is NavCommand.StartActivity<*, *> -> {
                // 这里只允许 ActivityRoute<A, Unit>
                val route = command.route as ActivityRoute<NavArgs, Unit>
                val args = command.args

                activityDriver.startActivityNow(route, args)
            }

            is NavCommand.ShowFragment<*, *> -> {
                // 这里只允许 FragmentRoute<A, Unit>
                val route = command.route as FragmentRoute<NavArgs, Unit>
                val args = command.args

                fragmentDriver.showFragmentNow(
                    route = route,
                    args = args,
                    containerId = command.containerId,
                    addToBackStack = command.addToBackStack,
                    replace = command.replace,
                )
            }

            is NavCommand.ShowDialog<*, *> -> {
                @Suppress("UNCHECKED_CAST")
                val route = command.route as DialogRoute<NavArgs, Unit>

                @Suppress("UNCHECKED_CAST")
                val args = command.args
                dialogDriver.showDialogNow(route, args, command.tag)
            }

            is NavCommand.PopBackStack -> {
                fragmentDriver.popBackStack()
            }
        }
    }
}
