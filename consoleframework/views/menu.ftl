<ul class="nav">
  <li <#if !(activePage??)>class="active"</#if>><a href="${baseUrl}/">Home</a></li>
  <li <#if activePage?? && activePage == "downloads">class="active"</#if>><a href="${baseUrl}/downloads.html">Downloads</a></li>
  <li><a href="https://github.com/elw00d/consoleframework">Github</a></li>
  <li class="dropdown">
    <a <#if activePage?? && activePage == "docs">class="active dropdown-toggle"<#else>class="dropdown-toggle"</#if> href="#" data-toggle="dropdown">Docs <b class="caret"></b></a>
    <ul class="dropdown-menu">
      <li><a href="${baseUrl}/getting-started.html">Getting started</a></li>
      <li><a href="${baseUrl}/build-from-sources.html">Building from sources</a></li>
      <li><a href="${baseUrl}/examples.html">Examples</a></li>
      <li class="divider"></li>
      <li><a href="${baseUrl}/xaml.html">XAML reference</a></li>
      <li><a href="${baseUrl}/binding.html">Data binding reference</a></li>
      <li><a href="${baseUrl}/layout.html">Layout system reference</a></li>
      <li><a href="${baseUrl}/custom-controls.html">Writing custom controls</a></li>
    </ul>
  </li>
</ul>