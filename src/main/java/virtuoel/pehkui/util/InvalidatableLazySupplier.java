package virtuoel.pehkui.util;

import java.util.Objects;
import java.util.function.Supplier;

public class InvalidatableLazySupplier<T> implements Supplier<T>
{
	final Supplier<T> supplier;
	boolean valid = false;
	T value;
	
	public InvalidatableLazySupplier(Supplier<T> delegate)
	{
		this.supplier = Objects.requireNonNull(delegate);
	}
	
	public void invalidate()
	{
		valid = false;
	}
	
	@Override
	public T get()
	{
		if (!valid)
		{
			final T t = supplier.get();
			value = t;
			valid = true;
			return t;
		}
		
		return value;
	}
}
