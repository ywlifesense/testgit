package cn.js.silverbullet.uikit.refreshlistview;

/**
 * Created by js on 5/26/15.
 */
public interface IListViewState {
    int LVS_NORMAL = 0;                    //  普通状态
    int LVS_PULL_REFRESH = 1;            //  下拉刷新状态
    int LVS_RELEASE_REFRESH = 2;        //  松开刷新状态
    int LVS_LOADING = 3;                //  加载状态
}
