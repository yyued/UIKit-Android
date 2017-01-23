package com.yy.codex.uikit

import android.content.Context

import java.util.ArrayList

/**
 * Created by cuiminghui on 2017/1/18.
 */

class UINavigationItem(val mContext: Context) {

    internal var navigationBar: UINavigationBar? = null

    /* Title */

    var title: String? = null
        set(title) {
            field = title
            updateTitle()
        }

    fun updateTitle() {
        (titleView as? UILabel)?.let {
            var titleView = it;
            val navigationBar = navigationBar as? UINavigationBar
            if (navigationBar != null && navigationBar.titleTextAttributes != null) {
                navigationBar.titleTextAttributes?.let {
                    if (title is String) {
                        titleView.attributedText = NSAttributedString(title as String, it)
                    }
                    else {
                        titleView.text = ""
                    }
                }
            }
            else {
                titleView.text = title
            }
            titleView.superview?.let(UIView::layoutSubviews)
        }
    }

    /* FrontView */

    internal var frontView: UINavigationItemView = UINavigationItemView(mContext);

    fun setNeedsUpdate() {
        for (subview in frontView.subviews) {
            subview.removeFromSuperview()
        }
        titleView?.let {
            updateTitle()
            frontView.addSubview(it)
            frontView.titleView = it
        }
        frontView.leftViews = leftBarButtonItems.mapNotNull { it.getContentView(mContext) }
        for (view in frontView.leftViews) {
            frontView.addSubview(view)
        }
        frontView.rightViews = rightBarButtonItems.mapNotNull { it.getContentView(mContext) }
        for (view in frontView.rightViews) {
            frontView.addSubview(view)
        }
    }

    /* TitleView */

    var titleView: UIView? = null
        get() {
            if (field == null) {
                val labelTitleView = UILabel(mContext)
                labelTitleView.font = UIFont(17f)
                labelTitleView.textColor = UIColor.blackColor
                field = labelTitleView
            }
            return field
        }
        set(titleView) {
            field?.let(UIView::removeFromSuperview)
            field = titleView
            setNeedsUpdate()
        }


    /* BarButtonItems */

    var leftBarButtonItems: List<UIBarButtonItem> = listOf()
        set(value) {
            field = value
            setNeedsUpdate()
        }

    fun setLeftBarButtonItem(leftBarButtonItem: UIBarButtonItem?) {
        if (leftBarButtonItem == null) {
            leftBarButtonItems = listOf()
        }
        else {
            leftBarButtonItems = listOf(leftBarButtonItem)
        }
    }

    var rightBarButtonItems: List<UIBarButtonItem> = listOf()
        set(value) {
            field = value
            setNeedsUpdate()
        }

    fun setRightBarButtonItem(rightBarButtonItem: UIBarButtonItem?) {
        if (rightBarButtonItem == null) {
            rightBarButtonItems = listOf()
        }
        else {
            rightBarButtonItems = listOf(rightBarButtonItem)
        }
    }

    var backBarButtonItemUsingMaterialDesign = false

    var backBarButtonItem: UIBarButtonItem? = null
        get() {
            val navigationBar = navigationBar ?: return null
            if (navigationBar.materialDesign && !backBarButtonItemUsingMaterialDesign && (field?.isSystemBackItem ?: false)) {
                field = null
            }
            if (field == null && navigationBar.materialDesign) {
                val arrowImage: UIImage
                if (UIScreen.mainScreen.scale() == 1.0) {
                    arrowImage = UIImage("iVBORw0KGgoAAAANSUhEUgAAAAkAAAAQCAYAAADESFVDAAAAAXNSR0IArs4c6QAAASRJREFUKBV1kc1LAlEUxc8bxVaBuygt06IiENq0Nqn/IBL/Qhsezj4IsmlfIEoQtGghFJUf26y8njtPGwbtwX0f9/zuufPeGPw3rJTxA0s5TC1lrBziFy1qBcbELEBNOcA3biFYo/aKDCpJyJc9TAgA6zB4QwonqJkn78/Jlx1W30QA8E7gVAHVHWSlSKDFyDH3yawCjwro8BBIgbdQYJMtBsycoW66TnazhzGa3OothgwF2k6KZ203nh3TdFqJpXin0AXFZ66rbHkFX45j2e3cEwSSxxdCpkqMUfThdfMwh93tzk0PaVTp+EIhS8drWDmaQ8nHvJRtCiGhLa79qLBmOklISwMpzVrn6fzBguoipKCVXf5gddzg6X455MB9gg263U0BPnZUSRYFI58AAAAASUVORK5CYII=")
                    arrowImage.scale = 1.0
                } else if (UIScreen.mainScreen.scale() == 2.0) {
                    arrowImage = UIImage("iVBORw0KGgoAAAANSUhEUgAAABIAAAAgCAYAAAAffCjxAAAAAXNSR0IArs4c6QAAAnhJREFUSA2dlstqFFEQhv/qGS8o6tYX8A0EEdzoUlwoyiQdiIhCJBqFLFRcBCFCFi4SEOM1qIhOxxGEwQtewLyAD5C8gRtBEAlGJ+Vf09M9c/qcudkQ+py/6nxTVV1dHcH/XCtaxjcsQHGax1cRYVyG5qSQhJBT+VnBp1K+GWRR0xJ+4gUhFcddsBE5Qq+NaoQGnhEyEnB7PFhqBknwhACriXsJFhHLVP+IVAXLeMTTPgR4gFFcMnJvUAq5z3TOumE0d0uIMQkRtV054NCWEtzhZqIttFaCp4xkIoOY2j2iZb1N+4XW0fZN8JyQc50QM4ZBic5jM829TeBKWPISzhCy6ehNU1Gp6i1KV4oyIa8IiVGRhmej4NYo0TkWNgR5jb0Yw+EwxMBtUKKzhFz3fk1Qxz5WZb/89WwdQlqjRGcImenQ06XgHdOpEPLHsxUENptOsrB3C7oV9gP24DiOym/PFhDKjOSGpws+syYnWJOBIHY+IigU9m6sY6v3Az2EiClc41+zzXM/xQH8YGp13ZVrfRYR39wqQRcDfgfxC+/xUXcGbJ6UPrVRuUfLZc8KHMJ3Prk3uiNgcyR3HiU6zSTnHQ/bCL6wDY6xq9c9W0tw37VYFlj+q56z4ginYx0rut2ztQQ3osyrqtbhc9k2v/forTDITqbdPptDsoXgLdM8yTQ3MsnubmqdllhucuuDlLVqoIavuqXTvXtEmVc6EUIvs02EEXZ/82XuDzJg9xlVY5pjNqMGAxnMpqZi2pbOlU7NIT/ZNsdDI5i1HDyiLIyqLnJZ/CisdX9q2cHiPcYUO/2hIwtWh4/ICPbhfMmJav/WKNawDef/AYBBp6FR2XEFAAAAAElFTkSuQmCC")
                    arrowImage.scale = 2.0
                } else {
                    arrowImage = UIImage("iVBORw0KGgoAAAANSUhEUgAAABsAAAAwCAYAAADgvwGgAAAAAXNSR0IArs4c6QAABDVJREFUWAm1mFuITVEYx791ZpCI3O+XcQmjcUu5FLmNSybkMuZIRBSlvCiFULzhUfIiCXs75ozcLzF4UBIlHjSePHgQymSMu/P5f+fMtte+rX3OmWPVPmut7/bb69trrb32UfQ/is3TiekoQk/E9ZoStIfWqtuq5Cybl1OGLiJuBy12hspodpkmaH/T5hqA0j6QxFUYac/SwWxegoANCNxRooeU1vIQYeEimxdiRALqZHB+2v5nZvF8AK5iVJ0NoI9UTlUJg0G8yuI5MLoSA/oEUDXVqnfFj8ziWYDcBKyL4a6aAVoA0DOxKW6CWDwTvrdwdZUgEeUzQAsBeuroC58gFk9rG5EJ1IKFvAigJw5I6sJgFk+Fz21c3cQ5tCj6AtAS7BiP/fr8J4jNUzCiO7i6+4No/VaAlgL0SJP9a+Y3QWyeCEgjrp7/PP0NRd/aQPf9KqcfP7IUV2HB3jWCiL4DtAwjigQJ0PzMUlxJf+ge7Ho7dxeoFf0AaAVAdwM6nyB6ZPU8FiBJXR+fj9tV9BOglQDJpIkt4SOr59EI0wjvfoYIvwBaDdANg41HFYTZPJJ+keR+gMdS7yj6je5agK7q4ri2N40prkDa7uMaFOmYAyUpqS5F2kQoXFiah+F+BTQkwlZegX+gWw9QfaSNQZFbZxYL4CFAFQbbDJ7RBqpT5ww2RlWCGrgXLGREcaBN7QHJXZRjOR5BPVI6oUVOD0RbAToTqi9AmMBzqDHaK9qGZ3TKaJOnUibIT6NthoYa9QUoE0jShRj7vWTxwRibvNQJ6kX7Yel5yQU8mQ4AuC8gL1CA96lqxbloEfyy54RIf6ZDAO6O1OehcN9n17gHtWCHZ5oc47eL1qljMTahancHqVGfcMSsxux8EWrpCo+SzTvdbv4td2SOT4r7YFOSRT7eEYXWinZgSRwP1UUIgzAxbOC+eCU+AHBchF/uU0HRdiz2k5E2PoWbRl2xUr3HiXIeRE262NPm7JfJCaR0i0du6ISPzHGweCCaskGPckSBOredbUZKTwd0PoEZJsZpHoyUPkRrhM9X72bQ2YhZelYX+tvhadStVqm32K7nImlvdLGvLXvsaTrPSZ/c040fmWNu8XA0JaXRe6W8XBUlMWnkMzdQ8oeJa5pHtKV0cCCSI5Bjg8L5pE7Jx6GnFAYT1xSPwv3LCGXyRBU5ea0B8LJuUDhMvFM8BkBZh/31YJ62nCkJZ8qkuu7I4yeIY6nXtaoJqZJ1+F4Xe9qc/ZBPY9IsduTFwcS7Tr3C7zxAPzjBAjVnP+gvAVgtuuLSqEc9zxPQbcQlB6fwougr/oKZ1H6YhE/xJMxBAfYIp0Gq6GzxadSj1qrnWPiSqmZd7GtXlgYmUeUfgfLsG/+zD+J0m0qTRiec1CmegZTKPwnud7d8w5XRlNLDcsAKrMPDaMrkeQ3QQYz85V9acAeH0YiWqgAAAABJRU5ErkJggg==")
                    arrowImage.scale = 3.0
                }
                field = UIBarButtonItem(arrowImage, this, "onBackButtonItemTouchUpInside")
                field?.insets = UIEdgeInsets.zero
                field?.isSystemBackItem = true
            }
            else if (field == null) {
                field = UIBarButtonItem(title ?: "Back", this, "onBackButtonItemTouchUpInside")
                if (UIScreen.mainScreen.scale() == 1.0) {
                    val arrowImage = UIImage("iVBORw0KGgoAAAANSUhEUgAAABoAAAAsCAYAAAB7aah+AAAAAXNSR0IArs4c6QAAATpJREFUWAm92LsNwjAQANAzFIzATJQwCAPQABJiAMagoUCCHegYgwkQlfEFQkLikPviJo5t3fNHyiUB8Cj7OGyGHTQb1Pe7OIIrHGERl/VYoX6jriNygwNEmLxjrWAb1li3g9pIOe8Cs4G6kQ/WOrSyh3ztR3Df7roV0ZAzjGEqhxgIzMNDBjERPAY+JED4kBDhQQqEDikRGmSA9ENGyG/IEOmGjJE85IC0ISfkG3JEKsgZeUF/QBCivpxEHKwpr6c3bVWnlMBmmFskYJUmaFiRLSVYBeE0HbFvyBFrQ05YHnLAuiFj7DdkiPVDRhgNMsDokBLjQQqMDwkxGSTA5BAT00EMjJr4MGS+YH5KH1rpu+ScH5BaA1z0Kyqjd6WYAaxhE1Z2EIJN7I1gly1Ux9J24Uqwya9kftE8AedMD8V9MQduAAAAAElFTkSuQmCC")
                    arrowImage.scale = 1.0
                    field?.image = arrowImage
                } else if (UIScreen.mainScreen.scale() == 2.0) {
                    val arrowImage = UIImage("iVBORw0KGgoAAAANSUhEUgAAABoAAAAsCAYAAAB7aah+AAAAAXNSR0IArs4c6QAAATpJREFUWAm92LsNwjAQANAzFIzATJQwCAPQABJiAMagoUCCHegYgwkQlfEFQkLikPviJo5t3fNHyiUB8Cj7OGyGHTQb1Pe7OIIrHGERl/VYoX6jriNygwNEmLxjrWAb1li3g9pIOe8Cs4G6kQ/WOrSyh3ztR3Df7roV0ZAzjGEqhxgIzMNDBjERPAY+JED4kBDhQQqEDikRGmSA9ENGyG/IEOmGjJE85IC0ISfkG3JEKsgZeUF/QBCivpxEHKwpr6c3bVWnlMBmmFskYJUmaFiRLSVYBeE0HbFvyBFrQ05YHnLAuiFj7DdkiPVDRhgNMsDokBLjQQqMDwkxGSTA5BAT00EMjJr4MGS+YH5KH1rpu+ScH5BaA1z0Kyqjd6WYAaxhE1Z2EIJN7I1gly1Ux9J24Uqwya9kftE8AedMD8V9MQduAAAAAElFTkSuQmCC")
                    arrowImage.scale = 2.0
                    field?.image = arrowImage
                } else {
                    val arrowImage = UIImage("iVBORw0KGgoAAAANSUhEUgAAACcAAABCCAYAAADUms/cAAAAAXNSR0IArs4c6QAAAbFJREFUaAXt2stNxDAQBuDfSNQBpWwVcORRBhHSXqALkOBCG1SCdkUfeD2CKC8ncex5+MAcR5b8aRxLmUmA/1iowKO/QuMvYivOYkm1XONv8IMPeHzGgE4NMt6IYMBrgLUFOsBhhyd3bJfa4Kaw1jMA6uPmYROgLm4dNgDq4dJhLfBLB7cV5sIdBu7lcZmwcGvfZHEFMDpbOVwhTA7HAJPBMcH4cYwwXhwzjA8nAOPBCcHKcYKwMpwwLB+nAMvDKcG24xRh23DKsHScASwNZwRbxxnClnHGsHlcBbA4rhLYFFcRbIirDNbhKoQR7nfC4zO6MB/mQ8LRtYY51fO4w7N7lzJ2ONqhMuAQlwsMcw0aH3BXcIrLBQoccRxXCXAeVwFwGWcMXMcZAtNwRsB0nAFwG04ZuB2nCMzDKQHzcQrAMpwwsBwnCOTBCQH5cAJAXhwzkB/HCJTB5QJHb9RyOAagLK4QKI8rAOrgCPjgb0Pr/tL7VYOyS6H0Gb0lpPbFDkecY6dXuVTgHwx7d9DHEXKugj0YLbPBxYAjmC2uDwS+6Rmjo6R0PdH4a+z9ZT2gRMkJbMiClVJCMU0AAAAASUVORK5CYII=")
                    arrowImage.scale = 3.0
                    field?.image = arrowImage
                }
                field?.imageInsets = UIEdgeInsets(0.0, 0.0, 0.0, 0.0)
                field?.isSystemBackItem = true
            }
            return field
        }
        set(backBarButtonItem) {
            field = backBarButtonItem
        }

    fun onBackButtonItemTouchUpInside() {
        var nextResponder: UIResponder? = frontView
        while (nextResponder != null) {
            nextResponder = nextResponder.nextResponder
            if (nextResponder is UINavigationController) {
                nextResponder.popViewController(true)
                return
            }
        }
    }

}
